import csrfHeader from "./csrfHeader.js";
import {addReaction, fetchReactions} from "./reactions.js";
import {getUser} from "./includes.js";

const {name, value} = csrfHeader();
const bodyElement = document.querySelector("body");
const youthCouncilID = +bodyElement.dataset.youthcouncil_id;
const button = document.querySelector(".join-council");
if (button !== null)
	button.addEventListener("click", addUserToCouncil);

const leaveBtn = document.querySelector(".leave-council");
if (leaveBtn !== null)
	leaveBtn.addEventListener("click", leaveYouthCouncil);

const buttons = document.querySelectorAll("button[id^=\"reaction-\"]");
buttons.forEach(button => {
	button.addEventListener("click", () => {
		addReaction(event, "action-point");
	});
});

const reactionBtn = document.querySelectorAll("button.react-btn");
const copyLinkBtn = document.querySelectorAll("button.copy-link-btn");
const getQrBtn = document.querySelectorAll("button.get-qr-btn");
const image_container = document.getElementById("qr-code-img");
const download_container = document.getElementById("qr-code-download");

reactionBtn.forEach(btn => btn.addEventListener("click", () => {
	getUser(true);
	fetchReactions(event, "action-point");
}));

copyLinkBtn.forEach(btn => btn.addEventListener("click", (event) => {
	if (event.target !== btn) {
		return;
	}
	navigator.clipboard.writeText(window.location.host + event.target.getAttribute("target-link"));
}));

getQrBtn.forEach(btn => btn.addEventListener("click", async (event) => {
	const url = window.location.host + event.target.getAttribute("target-link");
	const response = await fetch(
		`/api/qrcode/actionpoint/
					${btn.getAttribute("municipality")}/
					${btn.getAttribute("ap-id")}
		`, {
			method: "GET",
			headers: {
				youthCouncilID,
				[name]: value
			}
		});

	if (response.status > 399) {
		console.log("help!	");
		return;
	}

	const byte_array = await response.arrayBuffer();
	const blob = new Blob([byte_array], {type: "image/png"});
	const object_url = URL.createObjectURL(blob);
	image_container.src = object_url;
	download_container.setAttribute("href", object_url);
	download_container.setAttribute("download", "action-point-qr-code.png");
}));

function leaveYouthCouncil() {
	const userId = getUser(true);
	fetch(`/api/youthcouncils/${youthCouncilID}/${userId}`, {
		method: "DELETE",
		headers: {
			youthCouncilID,
			"Content-Type": "application/json",
			[name]: value
		}
	})
		.then((response) => {
			console.log(response);
			if (response.status === 200) {
				location.reload();

			}
		});
}

function addUserToCouncil() {

	const userId = getUser(true);
	fetch(`/api/youthcouncils/${youthCouncilID}/${userId}`, {
		method: "POST",
		headers: {
			youthCouncilID,
			"Content-Type": "application/json",
			[name]: value
		}
	})
		.then((response) => {
			console.log(response);
			if (response.status === 200) {
				location.reload();

			}
		});
}



