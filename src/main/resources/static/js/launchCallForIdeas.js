import csrfHeader from "./csrfHeader.js";

const button = document.querySelector("#launch-call-for-ideas");
button.addEventListener("click", async () => {
	const {name, value} = csrfHeader();
	const youthCouncilID = +document.querySelector("body").dataset.youthcouncil_id;
	const titleElement = document.querySelector("#c4i-title");
	const themeElement = document.querySelector("#c4i-theme");
	const title = titleElement.value;
	const theme = themeElement.value;
	console.log(theme);
	const res = await fetch(`/api/youthcouncils/${youthCouncilID}/callforideas`, {
		method: "POST",
		headers: {
			"youthCouncilID": youthCouncilID,
			"Content-Type": "application/json",
			Accept: "application/json",
			[name]: value
		},
		body: JSON.stringify({title, theme})
	});

	if (res.status === 200) {
		const data = await res.json();
		console.log(data);
		titleElement.value = "";
		const modal = bootstrap.Modal.getInstance(document.querySelector("#launch-call-for-ideas-modal"));
		modal.hide();
		return;
	}
	console.log(`Error: ${res.status}
	${await res.text()}`);
});
