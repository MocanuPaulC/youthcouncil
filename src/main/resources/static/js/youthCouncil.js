import csrfHeader from "./csrfHeader.js";

const {name, value} = csrfHeader();
let card = undefined;
let actionPointId = undefined;
let youthCouncilId = undefined;
let userId = undefined;
let prevReactionBtn = undefined;
const button = document.querySelector(".join-council");
if (button !== null)
	button.addEventListener("click", addUserToCouncil);

const buttons = document.querySelectorAll("button[id^=\"reaction-\"]");
buttons.forEach(button => {
	button.addEventListener("click", addReaction);
});

const reactionBtn = document.querySelectorAll("button[id^=\"expandBtn\"]");

reactionBtn.forEach(btn => btn.addEventListener("click", fetchReactions));


function fetchReactions() {
	if (event.target.classList.contains("show")) {
		console.log("gets inside");
		getNeededElements(event);
		getUser(false);
		let reactionBtns = document.getElementById("actionPoint_" + actionPointId).querySelectorAll("button[id^=\"reaction-\"]");
		if (userId !== undefined) {
			fetch(`/api/actionpointreaction/${actionPointId}/${userId}`, {
				method: "GET",
				headers: {
					"youthCouncilID": youthCouncilId,
					"Content-Type": "application/json",
					[name]: value
				}
			}).then((response) => {
				if (response.status === 200) {
					response.json().then((data) => {
						reactionBtns.forEach(btn => {
							changeReaction(btn, data);
						});
					});
				} else {
					reactionBtns.forEach(btn => {
						if (btn.classList.contains("clickedReaction"))
							btn.classList.remove("clickedReaction");
					});
				}

			});
			prevReactionBtn = event.target;
		}
	}
}


function addReaction() {
	const reaction = event.target.id.substring(event.target.id.indexOf("-") + 1).toUpperCase();
	const reactionCount = document.getElementById("reaction_count_" + actionPointId);

	getNeededElements(event);
	getUser(true);
	fetch(`/api/actionpointreaction/react`, {
		method: "POST",
		headers: {
			"youthCouncilID": youthCouncilId,
			"Content-Type": "application/json",
			[name]: value
		},
		body: JSON.stringify({
			"actionPointReactedOnId": actionPointId,
			"reaction": reaction,
			"reactingUserId": userId
		})
	})
		.then((response) =>
			response.json().then((data) => {
				reactionCount.innerText = parseInt(data.reactionCount);
			}));
}

function addUserToCouncil() {

	const youthCouncilId = document.getElementById("youthCouncilId").value;
	getUser(true);
	fetch(`/api/youthcouncils/${youthCouncilId}/${userId}`, {
		method: "POST",
		headers: {
			"youthCouncilID": youthCouncilId,
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

function changeReaction(btn, data) {
	btn.classList.remove("clickedReaction");
	if (btn.id === "reaction-" + data.reaction.toLowerCase()) {
		btn.classList.add("clickedReaction");
	}
}


function getNeededElements(event) {
	card = event.target.closest("div", {class: "card-header"});
	actionPointId = card.id.substring(card.id.indexOf("_") + 1);
	youthCouncilId = document.getElementById("youthCouncilId").value;
}

function getUser(needsLogin) {
	try {
		userId = document.getElementById("authUserId").value;
	} catch (e) {
		console.log("User not logged in");
		if (needsLogin) {
			window.location.href = "/login";
		}
	}
}