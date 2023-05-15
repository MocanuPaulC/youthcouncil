import csrfHeader from "./csrfHeader.js";

const youthCouncilID = +document.querySelector("body").dataset.youthcouncil_id;

const usernameDisplay = document.getElementById("username-display");
const usernameSubmit = document.getElementById("username-submit");
const usernameEditButton = document.getElementById("username-edit-button");
const usernameCancelButton = document.getElementById("username-cancel-button");
const usernameSaveButton = document.getElementById("username-save-button");

usernameEditButton.addEventListener("click", handleUsernameEditOnclick);
usernameSaveButton.addEventListener("click", handleUsernameEditSave);
usernameCancelButton.addEventListener("click", handleUsernameEditCancel);

let currentUsername;

function handleUsernameEditOnclick(event) {
	console.log("editing");
	usernameDisplay.setAttribute("contenteditable", "true");
	usernameEditButton.classList.add("d-none");
	usernameSaveButton.classList.remove("d-none");
	usernameCancelButton.classList.remove("d-none");
	currentUsername = usernameDisplay.innerText;
}

function handleUsernameEditCancel(event) {
	console.log("canceling");
	usernameDisplay.setAttribute("contenteditable", "false");
	usernameEditButton.classList.remove("d-none");
	usernameSaveButton.classList.add("d-none");
	usernameCancelButton.classList.add("d-none");
	usernameDisplay.innerText = currentUsername;
}

function handleUsernameEditSave(event) {
	console.log("saving");
	console.log(currentUsername);
	if (usernameDisplay.innerText === currentUsername) {
		handleUsernameEditCancel();
		return;
	}

	const userId = usernameSubmit.getAttribute("user-id");
	const {name, value} = csrfHeader();

	fetch(`/api/users/${userId}/username`, {
		method: "PATCH",
		headers: {
			"Accept": "application/json",
			"Content-Type": "application/json",
			"youthCouncilID": youthCouncilID,
			[name]: value
		},
		body: JSON.stringify({
			"newUsername": usernameDisplay.innerText
		})
	}).then(handleUsernameEditResponse);
}

function handleUsernameEditResponse(response) {
	console.log(response.status);
	if (response.status > 399) {
		//TODO handle response correctly
		console.log("Bad Response");
		return;
	}

	//TODO do something with the response
	response.json().then(x => console.log(x.response));
	handleUsernameEditCancel();
}
