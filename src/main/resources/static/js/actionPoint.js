import csrfHeader from "./csrfHeader.js";
import {getUser} from "./includes.js";

const {name, value} = csrfHeader();

const bodyElement = document.querySelector("body");
const youthCouncilID = +bodyElement.dataset.youthcouncil_id;
const subscribeButton = document.querySelector("#subscribe-btn");
const actionPointId = document.querySelector(".actionPointId").id.split("_")[1];
if (subscribeButton !== null)
	subscribeButton.addEventListener("click", subscribe);

function subscribe() {
	let userId = getUser(true);
	if (subscribeButton.classList.contains("btn-primary")) {
		subscribeToActionPoint(userId);
	} else {
		unsubscribe(userId);
	}
}

function subscribeToActionPoint(userId) {
	fetch(`/api/actionpoints/subscribe/${userId}/${actionPointId}`, {
			method: "POST",
			headers: {
				youthCouncilID,
				"Content-Type": "application/json",
				[name]: value
			}
		}
	).then((response) => {
		if (response.status === 200) {
			subscribeButton.classList.remove("btn-primary");
			subscribeButton.classList.add("btn-danger");
			subscribeButton.innerText = "Unsubscribe :(";
			console.log("subscribed");
		} else if (response.status === 403) {
			alert("You are not a member of this youth council. Please join the youth council to subscribe to action points.");
		}
	});
}

function unsubscribe(userId) {
	fetch(`/api/actionpoints/subscribe/${userId}/${actionPointId}`, {
			method: "DELETE",
			headers: {
				youthCouncilID,
				"Content-Type": "application/json",
				[name]: value
			}
		}
	).then((response) => {
		if (response.status === 200) {
			subscribeButton.classList.remove("btn-danger");
			subscribeButton.classList.add("btn-primary");
			subscribeButton.innerText = "Subscribe!";
			console.log("unsubscribed");
		} else if (response.status === 403) {
			alert("You are not a member of this youth council. Please join the youth council to subscribe to action points.");
		}
	});
}
