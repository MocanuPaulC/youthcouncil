import csrfHeader from "./csrfHeader.js";
import {sendMessage} from "./notifications.js";

const {name, value} = csrfHeader();
const youthCouncilID = +document.querySelector("body").dataset.youthcouncil_id;

const errorStyles = ["is-invalid", "border-3", "border-danger"];

const add_block_buttons = document.querySelectorAll(".add-block-button");
const action_point_container = document.getElementById("action-point-container");
const action_point_type = action_point_container.getAttribute("actionPoint-type");
const block_selection_overlay = document.getElementById("action-point-creation-overlay");
const subtheme_select = document.getElementById("subtheme-select");
const block_type_buttons = await fetchBlockTypes();
const action_point_submit = document.querySelector("button[button-type='submit']");

const icons = {
	up: `<i class="bi bi-arrow-up-circle-fill">`,
	down: `<i class="bi bi-arrow-down-circle-fill">`,
	trash: `<i class="bi bi-trash3-fill">`,
	add: `<i class="bi bi-plus-circle-fill">`
};
/**contains the information on where to insert the new block
 * @type{{location: string, type: string, content: string}}
 */
const block_add_state = {
	//contains the element before which the new element needs to be inserted
	location: null,
	//contains the type of element that needs to be inserted
	type: null,
	//contains possible content for the new object, this does not necessarily need to be filled.
	content: null
};

block_selection_overlay.addEventListener("click", event => {
	block_add_state.location = null;
	block_selection_overlay.style.display = "none";
});

for (let block of add_block_buttons) {
	block.addEventListener("click", addBlockEventHandler);
}

for (let button of block_type_buttons) {
	button.addEventListener("click", event => {
		event.preventDefault();
		block_add_state.type = event.target.getAttribute("type");
		block_selection_overlay.style.display = "none";
		addNewBlock(block_add_state);
	});
}


if (action_point_submit.getAttribute("submit-type") === "edit") {

	const type = action_point_type === "default";

	const municipality = type
		? null
		: window.location.pathname.split("/")[2];
	const title = type
		? window.location.pathname.split("/")[2]
		: window.location.pathname.split("/")[4];

	const path = type
		? `/api/actionpoints/actionpointblocks/${title}`
		: `/api/actionpoints/actionpointblocks/${municipality}/${title}`;

	const response = await fetch(path, {
		method: "GET",
		headers: {
			"youthCouncilID": youthCouncilID,
			"Content-Type": "application/json",
			"Accept": "application/json",
			[name]: value
		}
	});

	/**
	 * @type {Array<{
	 *     content: string,
	 *     type: string,
	 *     orderNumber: number
	 * }>}
	 */
	const json_res = await response.json();

	for (let i = 0; i < json_res.length; i++) {
		let current;
		for (let element of json_res) {
			if (element.orderNumber === i) {
				current = element;
				break;
			}
		}
		block_add_state.type = current.type;
		block_add_state.content = current.content;
		await addNewBlock();
	}


	document.querySelectorAll("textarea").forEach(block => resizeTextArea(block));
}


action_point_submit.addEventListener("click", handleFormSubmit);

function addBlockEventHandler(event) {
	console.log(event.target);
	console.log(event.target.getAttribute("block-position") === "end");
	console.log(action_point_container.lastChild.isEqualNode(event.target.closest(".action-point-block")));
	console.log(event.target.closest("button").getAttribute("block-position") === "end" ||
		action_point_container.lastChild.isEqualNode(event.target.closest(".action-point-block")));
	if (
		event.target.closest("button").getAttribute("block-position") === "end" ||
		action_point_container.lastChild.isEqualNode(event.target.closest(".action-point-block"))) {
		block_add_state.location = null;
	} else {
		block_add_state.location = event.target.closest(".action-point-block").nextSibling;
	}
	block_selection_overlay.style.display = "block";
}

async function addNewBlock() {
	const loc = block_add_state.location;
	block_add_state.location = null;
	const type = block_add_state.type;
	block_add_state.type = null;
	const content = block_add_state.content;
	block_add_state.content = null;
	const blockid = block_add_state.blockid;
	block_add_state.blockid = null;

	let new_node = document.createElement("div");
	if (loc == null) {
		action_point_container.appendChild(new_node);
	} else {
		action_point_container.insertBefore(new_node, loc);
	}
	new_node.classList.add("row");
	new_node.classList.add("action-point-block");
	new_node.setAttribute("node-type", type);

	let inner_node = document.createElement("input");

	const inner_node_container = document.createElement("div");

	inner_node_container.classList.add("col-10", "input-field");

	switch (type) {
		case "HEADER_BIG":
			inner_node.setAttribute("type", "text");
			inner_node.classList.add("h1");
			inner_node.value = content ? content : null;
			break;
		case "HEADER_MEDIUM":
			inner_node.setAttribute("type", "text");
			inner_node.classList.add("h2");
			inner_node.value = content ? content : null;
			break;
		case "HEADER_SMALL":
			inner_node.setAttribute("type", "text");
			inner_node.classList.add("h3");
			inner_node.value = content ? content : null;
			break;
		case "PARAGRAPH":
			inner_node = document.createElement("textarea");
			inner_node.classList.add("p");
			inner_node.addEventListener("keyup", (event) => resizeTextArea(event.target));
			inner_node.value = content ? content : null;
			break;
		case "IMAGE":
			if (content) {
				const result = await fetchMedia(content);
				if (result.error != null) {
					inner_node = document.createElement("div");
					inner_node.innerHTML = `ERROR: could not find image because of an error <br> ${result.error}`;
				} else {
					inner_node = document.createElement("img");
					inner_node.value = content ? content : null;
					if (result.media != null) {
						inner_node.src = result.media;
					}
				}

			} else {
				inner_node.setAttribute("type", "file");
				inner_node.setAttribute("accept", "image/jpeg, image/png, image/jpg");
				inner_node.addEventListener("change", handleImageSelection);
			}
			break;
		case "VIDEO":
			if (content) {
				const result = await fetchMedia(content);
				if (result.error != null) {
					inner_node = document.createElement("div");
					inner_node.innerHTML = `ERROR: could not find image because of an error <br> ${result.error}`;
					break;
				}
				inner_node = document.createElement("video");
				inner_node.setAttribute("value", content);
				inner_node.setAttribute("controls", null);
				const video_source = document.createElement("source");
				if (result.media != null) {
					video_source.src = result.media;
				}
				inner_node.appendChild(video_source);
			} else {
				inner_node.setAttribute("type", "file");
				inner_node.setAttribute("accept", "video/mp4");
				inner_node.addEventListener("change", handleImageSelection);
			}
			break;
		default:
			console.error("The js is not up to date with the Domain! Unexpected node found!");
	}

	inner_node.classList.add("w-100");

	new_node.appendChild(inner_node_container);
	inner_node_container.appendChild(inner_node);


	createBlockControls(new_node);
}

/**
 * @param {HTMLElement} parent_node
 */
function createBlockControls(parent_node) {

	const controls = document.createElement("div");
	controls.classList.add("col-2", "block-controls");

	const del_button = document.createElement("button");
	del_button.classList.add("del-button");
	del_button.innerHTML = icons.trash;
	const add_button = document.createElement("button");
	add_button.classList.add("add-button");
	add_button.innerHTML = icons.add;
	const up_button = document.createElement("button");
	up_button.classList.add("up-button");
	up_button.innerHTML = icons.up;
	const down_button = document.createElement("button");
	down_button.classList.add("down-button");
	down_button.innerHTML = icons.down;


	for (let button of [del_button, up_button, down_button, add_button]) {
		button.classList.add("btn");
		button.setAttribute("type", "button");
	}

	del_button.addEventListener("click", event => {
		event.target.closest(".action-point-block").remove();
	});
	del_button.classList.add("btn-danger");

	add_button.addEventListener("click", addBlockEventHandler);
	add_button.classList.add("btn-primary");

	up_button.addEventListener("click", event => {
		const parent = event.target.closest(".action-point-block");
		const sibling = parent.previousSibling;
		if (sibling) parent.parentElement.insertBefore(parent, sibling);
	});
	up_button.classList.add("btn-secondary");

	down_button.addEventListener("click", event => {
		const parent = event.target.closest(".row");
		const sibling = parent.nextSibling.nextSibling;
		if (sibling) {
			parent.parentElement.insertBefore(parent, sibling);
		} else parent.parentElement.appendChild(parent);
	});
	down_button.classList.add("btn-secondary");

	controls.append(del_button, up_button, add_button, down_button);


	parent_node.appendChild(controls);
}

async function fetchBlockTypes() {


	return await fetch(`/api/informativepages/blocktypes`, {
		method: "GET",
		headers: {
			"youthCouncilID": youthCouncilID,
			"Accept": "application/json",
			[name]: value
		}
	}).then(response => {
		if (response.status > 399) {
			console.error("Bad response");
			return;
		}
		return response.json().then(hydrateBlockTypes);
	});
}

/**
 * @param {Array<string>} data
 */
function hydrateBlockTypes(data) {
	const template = document.getElementById("block-type");
	const block_type_containter = template.parentElement;
	template.removeAttribute("id");
	template.classList.add("block-type");

	for (let block_type of data) {
		const new_block = template.cloneNode(true);
		block_type_containter.appendChild(new_block);
		new_block.setAttribute("type", block_type);
		new_block.innerText = block_type.replace("_", " ").toLowerCase();
	}

	template.remove();
	return document.querySelectorAll(".block-type");
}

async function handleImageSelection(event) {
	const media = event.target.files[0];

	const formData = new FormData();
	formData.append("media", media);
	const response = await fetch("/api/media/upload", {
		method: "POST",
		headers: {
			"youthCouncilID": youthCouncilID,
			"Accept": "application/json",
			[name]: value
		},
		body: formData
	});

	if (response.status > 399) {
		if (response.status === 400) {
			console.error("The image upload failed!");
		}
		return;
	}
	/**
	 * @type {{
	 *     path: string,
	 *     mediaType: string
	 * }}
	 */
	const json_res = await response.json();

	const parent = event.target.parentNode;
	event.target.remove();

	//FIXME use the returned path as source for the image

	if (json_res.mediaType === "IMAGE") {
		const imageContainer = document.createElement("img");
		imageContainer.setAttribute("value", json_res.path);
		imageContainer.setAttribute("src", URL.createObjectURL(media));
		imageContainer.classList.add("mw-100");

		parent.appendChild(imageContainer);
	} else if (json_res.mediaType === "VIDEO") {
		const videoContainer = document.createElement("video");
		videoContainer.setAttribute("controls", null);
		videoContainer.setAttribute("value", json_res.path);
		const sourceContainer = document.createElement("source");
		sourceContainer.setAttribute("src", URL.createObjectURL(media));
		sourceContainer.setAttribute("type", "video/mp4");
		videoContainer.classList.add("mw-100");

		videoContainer.appendChild(sourceContainer);
		parent.appendChild(videoContainer);
	}

}

async function handleFormSubmit(event) {
	document.querySelectorAll(".invalid-feedback").forEach(error => error.remove());


	const title_element = document.getElementById("action-point-title");
	const page_title = title_element.nodeName === "INPUT"
		? title_element.value.toString().replace(" ", "-")
		: title_element.innerText.replace(" ", "-");

	const is_create_type = action_point_submit.getAttribute("submit-type") === "create";
	const call_type = is_create_type ? "POST" : "PUT";

	const blocks = document.querySelectorAll(".action-point-block");
	let actionPointBlocks = [];

	for (let i = 0; i < blocks.length; i++) {
		const type = blocks[i].getAttribute("node-type");
		let content;
		switch (type) {
			case "HEADER_BIG":
			case "HEADER_MEDIUM":
			case "HEADER_SMALL":
				content = blocks[i].querySelector("input").value;
				errorStyles.forEach(cssClass => blocks[i].querySelector("input").classList.remove(cssClass));
				break;
			case "PARAGRAPH":
				content = blocks[i].querySelector("textarea").value;
				errorStyles.forEach(cssClass => blocks[i].querySelector("textarea").classList.remove(cssClass));
				break;
			case "IMAGE":
				content = blocks[i].querySelector("img").getAttribute("value");
				break;
			case "VIDEO":
				content = blocks[i].querySelector("video").getAttribute("value");
				break;
			default:
				console.error("The js is not up to date with the Domain! Unexpected node found!");
		}


		actionPointBlocks.push({
			"orderNumber": i,
			"content": content,
			"type": blocks[i].getAttribute("node-type")
		});
	}

	const municipality = window.location.pathname.split("/")[2];

	const path = action_point_type === "default"
		? `/api/actionpoints/${page_title}`
		: `/api/actionpoints/create/${municipality}/${page_title}/${subtheme_select.value}`;
	if (call_type === "PUT") {
		let label = document.querySelector(".ownsubtheme");
		if (label !== null && label.id !== subtheme_select.value) {
			sendMessage("actionPoint", page_title, label.innerText, subtheme_select.value, "status");
		} else {
			sendMessage("actionPoint", page_title, "", "", "not-status");
		}
	}

	const response = await fetch(path, {
		method: call_type,
		headers: {
			"youthCouncilID": youthCouncilID,
			"Content-Type": "application/json",
			"Accept": "application/json",
			[name]: value
		},
		body: JSON.stringify(actionPointBlocks)
	});

	if (response.status > 399) {
		if (response.status === 400) {
			console.error("Bad Response");

			const json_err = await response.json();
			const regex = /(?<=\[)[0-9]+(?=])/g;
			for (let error in json_err) {
				const errorFaultIndex = error.match(regex).at(0);
				addErrors(document.querySelectorAll(".action-point-block")[errorFaultIndex], json_err[error]);
			}
		} else if (response.status === 409) {
			alert("actionpoint with this title already exists for this YouthCouncil, please use a different name!");
		}


		return;
	} else {
		let uri = location.href.toString();
		if (uri.includes("callforideas")) {
			let cfiId = uri.split("/")[6];
			const ideas = document.getElementById("ideas");

			let ids = [];
			let nodes = ideas.getElementsByTagName("tr");
			for (let i = 0; i < nodes.length; i++) {
				let ideaTr = nodes[i];
				if (ideaTr.children[0].children[0].checked === true) {
					console.log(ideaTr.id + " is checked");
					ids.push({"ideaId": ideaTr.id});

				}
			}
			console.log(ids);

			fetch(`/api/actionpoints/${page_title}/linkideas/${cfiId}/${youthCouncilID}`,
				{
					method: "PATCH",
					headers: {
						"youthCouncilID": youthCouncilID,
						"Content-Type": "application/json",
						"Accept": "application/json",
						[name]: value
					}, body: JSON.stringify(ids)
				})
				.then(async response => {
					if (response.status > 399) {
						console.error("Bad Response");
						throw new Error("Bad Response");
					} else {
						/**
						 * @type{{
						 *     actionPointId: number,
						 *     title: String
						 * }}
						 */
						const json_res = await response.json();

						window.location.href = action_point_type === "default"
							? `/action-points/${json_res.title}`
							: `/youthcouncils/${municipality}/actionpoints/${json_res.actionPointId}`;

					}
				});

		}
	}


}

function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}

function resizeTextArea(target) {
	target.style.height = "1px";
	target.style.height = (25 + target.scrollHeight) + "px";
}

/**
 * @param {HTMLElement} invalidInputField,
 * @param {string} error
 */
function addErrors(invalidInputField, error) {
	const erroringField = invalidInputField.querySelector("input, textarea");
	errorStyles.forEach(style => erroringField.classList.add(style));

	const errorBox = document.createElement("div");
	errorBox.classList.add("invalid-feedback");
	errorBox.innerText = error;
	erroringField.parentNode.appendChild(errorBox);
}

/**
 * @returns {Promise<{
 *     media: string,
 *     error: string
 * }>}
 */
async function fetchMedia(imageName) {
	//TODO make this less hacky! This technically checks the name if it contains a slash meaning that
	// it would be a link to an image and not just a name. If it is a link just return it otherwise
	// fetch the blob and return that
	if (imageName.includes("/")) return {
		media: imageName,
		error: null
	};

	const response = await fetch(`/api/media/uploads/${imageName}`, {
		method: "GET",
		headers: {
			"youthCouncilID": youthCouncilID,
			[name]: value
		}
	});
	if (response.status > 399) {
		let message;
		if (response.status === 400) {
			message = "The provided file path needs to have a File extension.";
		} else if (response.status === 403) {
			message = "The provided path is not allowed!";
		} else if (response.status === 406) {
			message = "The File extension of the provided file is not correct!";
		} else if (response.status === 500) {
			message = "There was an internal error while fetching your image!";
		} else {
			message = "There was an undefined error while fetching your image!";
		}
		return {
			media: null,
			error: message
		};
	}

	const blob = await response.blob();
	return {
		media: URL.createObjectURL(blob),
		error: null
	};
}
