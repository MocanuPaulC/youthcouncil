import csrfHeader from "./csrfHeader.js";

const checkButtons = document.querySelectorAll(".form-check-input");

const {name, value} = csrfHeader();

let card = undefined;
let entityId = undefined;
let youthCouncilId = undefined;
let entity = undefined;
let displayStatus = undefined;
checkButtons.forEach(button => {
	button.addEventListener("click", changeDisplayStatus);
});

function changeDisplayStatus(event) {
	getNeededElements(event);
	const isDisplayed = !!event.target.checked;
	let params = {
		"displayed": isDisplayed
	};

	fetch(`/api/${entity}/${entityId}/set-display`, {
		method: "PATCH", headers: {
			"Content-Type": "application/json", "youthCouncilID": youthCouncilId, [name]: value
		}, body: JSON.stringify(params)

	})
		.then(response => {
			if (response.status === 200) {

				displayStatus.innerText = isDisplayed ? "DISPLAYED" : "HIDDEN";
				console.log("success");
			} else {
				console.log("error");
			}
		});

}

function getNeededElements(event) {
	card = event.target.parentNode.parentNode;
	entity = card.id.substring(0, card.id.indexOf("_"));
	entityId = card.id.substring(card.id.indexOf("_") + 1);
	youthCouncilId = document.getElementById("youthCouncilId").value;
	displayStatus = event.target.previousElementSibling;
}
