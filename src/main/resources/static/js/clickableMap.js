import csrfHeader from "./csrfHeader.js";

const paths = document.querySelectorAll("path[nis]");
const overlay = document.getElementById("map-belgium-overlay");
const overlay_container = overlay.querySelector(".overlay-container");
overlay_container.addEventListener("click", function () {
	if (event.target.nodeType === "A" || event.target.nodeType === "BUTTON") {
		console.log("exiting early");
		return;
	}
	overlay.style.display = "none";
});

const l = parseInt(getComputedStyle(overlay.parentElement).getPropertyValue("--pointer-height").slice(0, -2));
overlay.parentElement.style.setProperty("--pointer-sqrt", `${Math.sqrt(Math.pow(l, 2) * 2) / 2}px`);

getMunicipaityData();


for (const municipality of paths) {
	municipality.addEventListener("click", mapSelectMunicipality);
}

/*
overlay.addEventListener("click", (event) => {
	overlay.style.display = "none";
});*/

function mapSelectMunicipality(event) {
	overlay.style.display = "block";
	overlay.style.top = `${event.clientY}px`;
	overlay.style.left = `${event.clientX}px`;
	overlay.style.right = "";

	const target = event.target;
	const text_container = overlay.querySelector(".overlay-container > .overlay-text");
	// should maybe be a call to the json instead of grabbing it from the svg
	//although doing a fetch on each click could hamper perforance
	const municipality_name = target.getAttribute("n");

	text_container.innerText = municipality_name;

	const status = target.getAttribute("status");
	const buttons = overlay.querySelector(".overlay-buttons");
	const link = buttons.querySelector(".link-to-yc");
	const create = buttons.querySelector(".link-yc-create");
	const sorry = overlay.querySelector(".overlay-sorry");
	if (status === "JOINED" || status === "YOUTHCOUNCIL_EXISTS") {
		link.style.display = "block";
		if (create) create.style.display = "none";
		if (sorry) sorry.style.display = "none";
		link.setAttribute("href", `/youthcouncils/${municipality_name}`);
	} else {
		if (create) create.setAttribute("href", `/youthcouncils/add/${municipality_name}`);
		link.style.display = "none";
		if (create) create.style.display = "block";
		if (sorry) sorry.style.display = "block";
	}


	overlay_container.style.left = "";

	if (overlay.getBoundingClientRect().left < 0) {
		overlay_container.style.left = `${overlay.getBoundingClientRect().left * -1}px`;
	} else if (overlay.getBoundingClientRect().right > window.innerWidth) {
		overlay_container.style.left = `-${overlay.getBoundingClientRect().right - window.innerWidth}px`;
	}
}


function getMunicipaityData() {
	const {name, value} = csrfHeader();
	const youthCouncilID = +document.querySelector("body").dataset.youthcouncil_id;


	fetch(`/api/municipalities/`, {
		method: "GET",
		headers: {
			"youthCouncilID": youthCouncilID,
			"Accept": "application/json",
			[name]: value
		}
	}).then(handleGetMunicipalities);
}

function handleGetMunicipalities(response) {
	if (response.status > 399) {
		console.log("Bad response");
		return;
	}

	response.json().then(data => {
		for (const muncilipality of paths) {
			let nis = muncilipality.getAttribute("nis");
			muncilipality.setAttribute("status", data[nis]);
		}
	});
}
