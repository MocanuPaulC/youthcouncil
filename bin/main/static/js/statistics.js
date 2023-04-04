import csrfHeader from "./csrfHeader.js";

const {name, value} = csrfHeader();

const buttons = document.querySelectorAll(".promote, .demote");
const roles = {
	MODERATOR: "MODERATOR",
	MEMBER: "MEMBER"
};


for (const btn of buttons) {
	btn.addEventListener("click", updateUser);
}

function updateUser() {
	const tableRow = event.target.closest("tr");
	const userId = +tableRow.id.substring(tableRow.id.indexOf("_") + 1);
	const userRole = tableRow.getElementsByClassName("role")[0];
	const button = event.target;
	let roleToSet = "";
	if (userRole.innerText === roles.MEMBER) {
		roleToSet = roles.MODERATOR;
	} else {
		roleToSet = roles.MEMBER;
	}

	fetch(`/api/users/${userId}`, {
		method: "PATCH",
		headers: {
			"Content-Type": "application/json",
			[name]: value
		},
		body: JSON.stringify({role: roleToSet})
	})
		.then((response) => {
			handleUpdateResponse(response, button, userRole);
		});


}

function handleUpdateResponse(response, button, userRole) {
	if (response.status === 204) {
		if (userRole.innerText === roles.MEMBER) {
			userRole.innerText = roles.MODERATOR;
			button.innerText = "Demote";
		} else {
			userRole.innerText = roles.MEMBER;
			button.innerText = "Promote";
		}
		button.classList.toggle("btn-primary");
		button.classList.toggle("btn-danger");
	} else {
		throw new Error("Something went wrong");
	}
}
