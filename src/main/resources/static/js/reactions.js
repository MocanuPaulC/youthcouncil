//reactions.js
import {getUser} from "./includes.js";
import csrfHeader from "./csrfHeader.js";

const {name, value} = csrfHeader();


let card = undefined;
let entityId = undefined;
const bodyElement = document.querySelector("body");
const youthCouncilID = +bodyElement.dataset.youthcouncil_id;
// let prevReactionBtn = undefined;
let userId = undefined;


export function fetchReactions(event, entity) {
	if (event.target.classList.contains("show")) {
		console.log("gets inside");
		getNeededElements(event);
		userId = getUser(false);
		const reactionPad = entity + "-reaction";
		let reactionBtns = document.getElementById("entity_" + entityId).querySelectorAll("button[id^=\"reaction-\"]");
		if (userId !== undefined) {
			fetch(`/api/${reactionPad}/${entityId}/${userId}`, {
				method: "GET",
				headers: {
					youthCouncilID,
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
			// prevReactionBtn = event.target;
		}
	}
}


export function addReaction(event, entity) {
	getNeededElements(event);
	const reaction = event.target.id.substring(event.target.id.indexOf("-") + 1).toUpperCase();
	const reactionCount = document.getElementById("reaction_count_" + entityId);
	const reactionPad = entity + "-reaction";
	userId = getUser(true);
	fetch(`/api/${reactionPad}/react`, {
		method: "POST",
		headers: {
			youthCouncilID,
			"Content-Type": "application/json",
			[name]: value
		},
		body: JSON.stringify({
			"entityReactedOnId": entityId,
			"reaction": reaction,
			"reactingUserId": userId
		})
	})
		.then((response) =>
			response.json().then((data) => {
				reactionCount.innerText = parseInt(data.reactionCount);
			}));
}

export function changeReaction(btn, data) {
	btn.classList.remove("clickedReaction");
	if (btn.id === "reaction-" + data.reaction.toLowerCase()) {
		btn.classList.add("clickedReaction");
	}
}

function getNeededElements(event) {
	card = event.target.closest("div", {class: "card-header"});
	entityId = card.id.substring(card.id.indexOf("_") + 1);
}

