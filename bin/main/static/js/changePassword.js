import csrfHeader from "./csrfHeader.js";

const passwordField = document.getElementById("password");
const confirmPasswordField = document.getElementById("confirm-password");
const resetPasswordSubmit = document.getElementById("resetpassword-submit");


resetPasswordSubmit.addEventListener("click", handleResetOnclick);


function handleResetOnclick(event) {
	const passw_1 = passwordField.value;
	const passw_2 = confirmPasswordField.value;
	if (passw_1 !== passw_2) {
		//TODO add frontend validation for the user!
		console.log("The passwords need to be the same.");
		return;
	}

	const userId = resetPasswordSubmit.getAttribute("user-id");

	const {name, value} = csrfHeader();

	fetch("/api/users/" + userId + "/resetpassword", {
		method: "POST",
		headers: {
			"Accept": "application/json",
			"Content-Type": "application/json",
			[name]: value
		},
		body: JSON.stringify({
			"password": passw_1,
			"confirmPassword": passw_2
		})
	}).then(handlePasswordResetResponse);
}

function handlePasswordResetResponse(response) {
	console.log(response.status);
	passwordField.value = "";
	confirmPasswordField.value = "";
	if (response.status > 399) {
		//TODO handle response correctly
		console.log("Bad Response");
		return;
	}

	//TODO do something with the response
	response.json().then(x => console.log(x.response));
}
