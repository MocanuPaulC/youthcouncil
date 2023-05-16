import csrfHeader from "./csrfHeader.js";

const loggedIn = document.getElementById("authUserIdNavBar");
const {name, value} = csrfHeader();

let privateStompClient = null;

const dropdownBtn = document.getElementById("notification-dropdown");
const notificationContainer = document.getElementById("notification-container");

if (dropdownBtn) {
	notificationContainer.childNodes.forEach(child => {
		if (child instanceof HTMLElement && child.classList.contains("notification-item-not-seen")) {
			dropdownBtn.classList.replace("btn-dark", "btn-danger");
		}
	});
	dropdownBtn.addEventListener("click", () => {
		const userId = loggedIn.value;
		if (dropdownBtn.classList.contains("btn-danger")) {
			fetch(`/api/notifications/${userId}`, {
				method: "PATCH",
				headers: {
					"Content-Type": "application/json",
					[name]: value
				}
			})
				.then(handleNotificationClick);
		}
	});
}


function handleNotificationClick(response) {
	if (response.status === 200) {
		dropdownBtn.classList.replace("btn-danger", "btn-dark");
		notificationContainer.childNodes.forEach(child => {
			if (child instanceof HTMLElement && child.classList.contains("notification-item-not-seen")) {
				child.classList.replace("notification-item-not-seen", "notification-item-seen");
			}
		});
	}
}


// geneva convention? more like, geneva suggestion

// I think i will get a tattoo of this xD

if (loggedIn) {
	privateStompClient = Stomp.over(new SockJS("/ws"));
	privateStompClient.connect({}, frame => {
		privateStompClient.subscribe("/user/specific", result => showMessageOutput(JSON.parse(result.body)));
	});
}


function createNotification(messageOutput, granted) {
	const {entityType, title, field, oldValue, newValue} = messageOutput;
	const message = field === "status"
		? `The ${entityType} ${title} has been updated from ${oldValue} to ${newValue}`
		: `The ${entityType} ${title} has received changes to its ${field}`;

	const dropdown = document.querySelector(".dropdown-menu-notification");
	const newNotification = dropdown.insertBefore(document.createElement("div"), dropdown.firstChild);
	newNotification.textContent = message;
	newNotification.classList.add("dropdown-item");

	dropdownBtn.classList.replace("btn-dark", "btn-danger");
	if (granted) new Notification(message);

	return newNotification;
}


function showMessageOutput(messageOutput) {

	if (!("Notification" in window)) alert("This browser does not support desktop notification");
	else Notification.requestPermission().then(permission => createNotification(messageOutput, permission === "granted").classList.add("notification-item-not-seen"));

}

export function sendMessage(entityType, title, oldValue, newValue, field) {
	let entityId = null;

	if (entityType === "actionPoint") {
		entityId = +document.querySelector(".actionPointId").id.split("_")[1];
	}

	privateStompClient.send("/app/private", {}, JSON.stringify({
		entityType,
		title,
		oldValue,
		newValue,
		field,
		entityId
	}));
}
