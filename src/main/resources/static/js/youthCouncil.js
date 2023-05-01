import csrfHeader from "./csrfHeader.js";
import {addReaction, fetchReactions} from "./reactions.js";
import {getUser} from "./includes.js";

const {name, value} = csrfHeader();
const bodyElement = document.querySelector("body");
const youthCouncilID = +bodyElement.dataset.youthcouncil_id;
const button = document.querySelector(".join-council");
if (button !== null)
	button.addEventListener("click", addUserToCouncil);

const buttons = document.querySelectorAll("button[id^=\"reaction-\"]");
buttons.forEach(button => {
	button.addEventListener("click", () => {
		addReaction(event, "action-point");
	});
});

const reactionBtn = document.querySelectorAll("button[id^=\"expandBtn\"]");

reactionBtn.forEach(btn => btn.addEventListener("click", () => {
	fetchReactions(event, "action-point");
}));


function addUserToCouncil() {

	const youthCouncilId = document.getElementById("youthCouncilId").value;
	const userId = getUser(true);
	fetch(`/api/youthcouncils/${youthCouncilId}/${userId}`, {
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


