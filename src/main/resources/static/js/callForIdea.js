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

const reactionBtn = document.querySelectorAll("button.react-btn");

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
		console.error(`${response.status}: Something went wrong`);
		return;
	}

	/**
	 * @type {{
	 * 	ideaId: number,
	 * 	idea: string,
	 * 	imagePath: string | null,
	 * 	username:string,
	 * 	subTheme:string
	 * }}
	 */
	const data = await response.json();
	console.log(data);
	const newIdea = document.querySelector(".idea").cloneNode(true);
	const username = newIdea.querySelector(".idea-user");
	username.innerText = data.username;
	const sub_theme = newIdea.querySelector(".idea-theme");
	sub_theme.innerText = data.subTheme;
	const idea_idea = newIdea.querySelector(".idea-idea");
	idea_idea.innerText = data.idea;
	const idea_reaction_dropdown = newIdea.querySelector(".idea-reaction-dropdown");
	idea_reaction_dropdown.id = `id_${data.ideaId}`;
	const buttons = idea_reaction_dropdown.querySelectorAll("button");
	for (const button of buttons) {
		button.addEventListener("click", (event) => {
			addReaction(event, "idea");
		});
	}
	const idea_reaction_select = newIdea.querySelector(".idea-reaction-select");
	idea_reaction_select.id = `entity_${data.ideaId}`;
	const idea_reaction_counter = newIdea.querySelector(".idea-reaction-counter");
	idea_reaction_counter.id = `reaction_count_${data.ideaId}`;
	idea_reaction_counter.innerText = "0";

	ideas.appendChild(newIdea);
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
