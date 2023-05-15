import csrfHeader from "./csrfHeader.js";

const deleteButton = document.getElementById("delete-btn");
if (deleteButton != null)
	deleteButton.addEventListener("click", deleteUser);

async function deleteUser() {
	const userId = document.getElementById("username-submit").value;
	const {name, value} = csrfHeader();
	fetch(`/api/users/${userId}`, {
		method: "DELETE",
		headers: {
			"Content-Type": "application/json",
			[name]: value
		}
	})
		.then((response) => {
			handleDeleteResponse(response);
		});
}

function handleDeleteResponse(response) {
	if (response.status === 204) {
		location.href = "/";
	}
}
