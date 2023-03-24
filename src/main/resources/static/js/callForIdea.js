import csrfHeader from "./csrfHeader.js";

const {name, value} = csrfHeader();

const submitIdea = document.querySelector(".submit-idea");
const ideaText = document.querySelector("#idea-text");
const hiddenUserId = document.querySelector(".hiddenUserId");
const hiddenCFIId = document.querySelector(".hiddenCFIId");
const callForIdeaId = +hiddenCFIId.id.substring(hiddenCFIId.id.indexOf("_") + 1);
const userId = +hiddenUserId.id.substring(hiddenUserId.id.indexOf("_") + 1);


submitIdea.addEventListener("click", handleIdeaSubmission);


function handleIdeaSubmission() {

	fetch(`/api/ideas/${userId}/${callForIdeaId}`, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[name]: value
		},
		body: JSON.stringify({title: ideaText.value})
	})
		.then((response) => {
			handlePost(response);
		});

}

function handlePost(response) {
	const ideas = document.querySelectorAll(".idea").item(document.querySelectorAll(".idea").length - 1);
	if (response.ok) {
		response.json().then((data) => {

			// this is for the sake of display.
			// In the future, the ideas will not be immediately displayed
			// because they will first have to go through the moderation process
			const newIdea = document.createElement("section");
			const ideaSpan = document.createElement("span");
			ideaSpan.innerText = data["title"].valueOf();
			newIdea.insertAdjacentElement("beforeend", ideaSpan);
			ideas.insertAdjacentElement("afterend", newIdea);
		});
		console.log("Great success");
	} else {
		console.log("Pain in my Borat");
	}
}