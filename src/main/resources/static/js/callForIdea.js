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
const submitCSVbtn = document.getElementById("submit-csv-btn");

const submitAnyway = document.getElementById("submit-anyway");
submitAnyway.addEventListener("click", () => sendCSVFile(convertToCSV(validRows)));

submitIdea.addEventListener("click", handleIdeaSubmission);
submitCSVbtn.addEventListener("click", handleCSVSubmission);
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
	const btnToAdd = document.querySelector(".dropup");
	const newBtn = btnToAdd.cloneNode(true);
	newBtn.getElementsByTagName("div")[0].id = "entity_" + data.ideaId;
	newBtn.getElementsByTagName("span")[0].id = "reaction_count_" + data.ideaId;
	newBtn.getElementsByTagName("span")[0].innerText = "0";
	let newBtns = newBtn.getElementsByTagName("button");
	for (let i = 1; i < newBtns.length; i++) {
		newBtns[i].addEventListener("click", () => {
			addReaction(event, "idea");
		});
	}
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
	newIdeaElement.append(newBtn);
	ideaElement.innerText = data.idea;
}

function parseCSV(file) {
	const reader = new FileReader();

	return new Promise((resolve, reject) => {
		reader.onload = () => {
			const csvData = reader.result;
			const lines = csvData.split("\n");
			const headers = lines[0].split(",");
			const data = [];

			for (let i = 1; i < lines.length - 1; i++) {
				const values = lines[i].split(",");
				const row = {};

				for (let j = 0; j < headers.length; j++) {
					row[headers[j]] = values[j];
				}

				data.push(row);
			}

			resolve(data);
		};

		reader.onerror = () => {
			reject(reader.error);
		};

		reader.readAsText(file);
	});
}

let validRows = [];

function handleCSVSubmission() {
	const input = document.getElementById("csv-to-submit");
	const file = input.files[0];
	let subthemes = [];
	let invalidRows = [];
	validRows = [];
	let rowNumber = 2;
	fetch(`/api/ideas/${callForIdeaId}/subthemes`, {
		method: "GET",
		headers: {
			youthCouncilID,
			[name]: value
		}
	}).then(response => response.json())
		.then(data => {
			subthemes = data;
			parseCSV(file)
				.then(parsedData => {
					parsedData.forEach(row => {
						const noneMatch = subthemes.every(subtheme => subtheme !== row.SubTheme);
						if (noneMatch) {
							invalidRows.push(rowNumber);
						} else {
							validRows.push(row);
						}
						rowNumber++;
					});
					handlePartialSubmission(invalidRows, parsedData);
				})
				.catch(error => {
					console.error("Error parsing CSV:", error);
				});

		})
		.catch(error => {
			console.error("Error:", error);
		});

}


function handlePartialSubmission(invalidRows, parsedData) {
	const errorParagraph = document.getElementById("csv-error");
	if (invalidRows.length > 0) {
		errorParagraph.hidden = false;
		errorParagraph.innerText = `${validRows.length} out of ${invalidRows.length + validRows.length} rows correctly imported \n
 								The following rows have issues with the subtheme: ${invalidRows.join(", ")}`;
		submitAnyway.hidden = false;
		submitCSVbtn.innerText = "Reupload";
		submitCSVbtn.classList.remove("btn-primary");
		submitCSVbtn.classList.add("btn-danger");
	} else {
		errorParagraph.hidden = true;
		submitAnyway.hidden = true;
		submitCSVbtn.innerText = "Upload";
		submitCSVbtn.classList.add("btn-primary");
		submitCSVbtn.classList.remove("btn-danger");
		sendCSVFile(convertToCSV(parsedData));
	}
}


function sendCSVFile(file) {
	const formData = new FormData();
	formData.append("file", file); // Append the file to FormData
	fetch(`/api/ideas/${callForIdeaId}/uploadFile`, {
		method: "POST",
		headers: {
			youthCouncilID,
			[name]: value
		},
		body: formData
	})
		.then(response => response.text())
		.then(data => {
			location.reload();
		})
		.catch(error => {
			console.error("Error:", error);
		});
}


function convertToCSV(objects) {
	const headers = Object.keys(objects[0]);
	const rows = objects.map(obj => headers.map(header => obj[header]));
	rows.unshift(headers);
	const csv = rows.map(row => row.join(",")).join("\n");
	return new Blob([csv], {type: "text/csv"});
}
