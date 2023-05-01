import csrfHeader from "./csrfHeader.js";
import {addReaction, fetchReactions} from "./reactions.js";
import {getUser} from "./includes.js";

const {name, value} = csrfHeader();

const submitIdea = document.querySelector(".submit-idea");
const ideaText = document.querySelector("#idea-text");
const bodyElement = document.querySelector("body");
const youthCouncilID = +bodyElement.dataset.youthcouncil_id;
let userId = undefined;
const callForIdeaId = +bodyElement.dataset.callForIdeaId;
const ideas = document.querySelector(".ideas");
const subThemeElement = document.querySelector("#subtheme");


submitIdea.addEventListener("click", handleIdeaSubmission);

const buttons = document.querySelectorAll("button[id^=\"reaction-\"]");
buttons.forEach(button => {
	button.addEventListener("click", () => {
		addReaction(event, "idea");
	});
});

const reactionBtn = document.querySelectorAll("button[id^=\"expandBtn\"]");

reactionBtn.forEach(btn => btn.addEventListener("click", () => {
	fetchReactions(event, "idea");
}));


async function handleIdeaSubmission() {
	const subThemeId = +subThemeElement.value;
	userId = getUser(true);
	const body = {
		userId,
		callForIdeaId,
		idea: ideaText.value,
		subThemeId
	};

	const response = await fetch(`/api/ideas`, {
		method: "POST",
		headers: {
			youthCouncilID,
			"Content-Type": "application/json",
			[name]: value
		},
		body: JSON.stringify(body)
	});

	await handlePost(response);
}

async function handlePost(response) {
	if (!response.ok) {
		console.log("Something went wrong");
		return;
	}
	/**
	 * @type {{idea: string, imagePath: string | null, username:string}}
	 */
	const data = await response.json();

	const newIdeaElement = document.createElement("div");
	ideas.append(newIdeaElement);
	newIdeaElement.classList.add("idea");
	const usernameElement = document.createElement("p");
	newIdeaElement.append(usernameElement);
	usernameElement.classList.add("text-muted", "mb-1");
	const spanElement = document.createElement("span");
	spanElement.classList.add("badge", "rounded-pill", "text-bg-info");
	spanElement.innerText = subThemeElement.options[subThemeElement.selectedIndex].text;
	usernameElement.innerText = `${data.username} `;
	usernameElement.append(spanElement);
	const ideaElement = document.createElement("p");
	newIdeaElement.append(ideaElement);
	ideaElement.innerText = data.idea;
}
