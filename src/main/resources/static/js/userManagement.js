import csrfHeader from "./csrfHeader.js";

const roles = {
	MODERATOR: "MODERATOR",
	USER: "USER"
};
const {name, value} = csrfHeader();

const promoteButtons = document.querySelectorAll(".promote, .demote");
const blockButtons = document.querySelectorAll(".block, .unblock");
const deleteButtons = document.querySelectorAll(".delete-btn");

for (const btn of deleteButtons) {
	btn.addEventListener("click", deleteUser);
}
for (const btn of promoteButtons) {
	btn.addEventListener("click", updateUserRole);
}
for (const btn of blockButtons) {
	btn.addEventListener("click", updateUserBlockedStatus);
}

function deleteUser() {
	const tableRow = event.target.closest("tr");
	const userId = +tableRow.id.substring(tableRow.id.indexOf("_") + 1);
	const button = event.target;
	fetch(`/api/users/${userId}`, {
		method: "DELETE",
		headers: {
			"Content-Type": "application/json",
			[name]: value
		}
	})
		.then((response) => {
			handleDeleteResponse(response, button);
		});
}


function updateUserBlockedStatus() {
	const tableRow = event.target.closest("tr");
	const userId = +tableRow.id.substring(tableRow.id.indexOf("_") + 1);

	const youthCouncilId = +document.getElementById("youthCouncilId").value;
	const button = event.target;
	let statusToSet = "";
	statusToSet = button.innerText === "Block";
	console.log("status to set : " + statusToSet);
	fetch(`/api/users/${userId}/blocked-status`, {
		method: "PATCH",
		headers: {
			"Content-Type": "application/json",
			"youthCouncilId": youthCouncilId,
			[name]: value
		},
		body: JSON.stringify({blockedStatus: statusToSet, youthCouncilId: youthCouncilId})
	})
		.then((response) => {
			handleStatusUpdateResponse(response, button);
		});
}

function updateUserRole() {
	const tableRow = event.target.closest("tr");
	const userId = +tableRow.id.substring(tableRow.id.indexOf("_") + 1);
	const userRole = tableRow.getElementsByClassName("role")[0];
	const youthCouncilId = +document.getElementById("youthCouncilId").value;
	console.log(userRole);
	console.log(tableRow);
	console.log(youthCouncilId);
	const button = event.target;
	let roleToSet = "";
	if (userRole.innerText === roles.USER) {
		roleToSet = roles.MODERATOR;
	} else {
		roleToSet = roles.USER;
	}

	fetch(`/api/users/${userId}/role`, {
		method: "PATCH",
		headers: {
			"Content-Type": "application/json",
			"youthCouncilId": youthCouncilId,
			[name]: value
		},
		body: JSON.stringify({role: roleToSet, youthCouncilId: youthCouncilId})
	})
		.then((response) => {
			handleRoleUpdateResponse(response, button, userRole);
		});


}

function handleDeleteResponse(response, button) {
	if (response.status === 204) {
		const tableRow = button.closest("tr");
		tableRow.remove();
	} else {
		throw new Error("Something went wrong");
	}
}

function handleStatusUpdateResponse(response, button) {
	if (response.status === 204) {
		if (button.innerText === "Block") {
			button.innerText = "Unblock";
		} else {
			button.innerText = "Block";
		}
	} else {
		throw new Error("Something went wrong");
	}

}

function handleRoleUpdateResponse(response, button, userRole) {
	if (response.status === 204) {
		if (userRole.innerText === roles.USER) {
			userRole.innerText = roles.MODERATOR;
			button.innerText = "Demote";
		} else {
			userRole.innerText = roles.USER;
			button.innerText = "Promote";
		}
		button.classList.toggle("btn-primary");
		button.classList.toggle("btn-danger");
	} else {
		throw new Error("Something went wrong");
	}
}
