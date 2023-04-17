import csrfHeader from "./csrfHeader.js";

const {name, value} = csrfHeader();

const button = document.querySelector(".join-council");

button.addEventListener("click", addUserToCouncil);


function addUserToCouncil() {

	const youthCouncilId = document.getElementById("youthCouncilId").value;
	let userId = undefined;
	try {
		userId = document.getElementById("authUserId").value;
	} catch (e) {
		console.log("User not logged in");
		window.location.href = "/login";
	}
	fetch(`/api/youthcouncils/${youthCouncilId}/${userId}`, {
		method: "POST",
		headers: {
			"youthCouncilID": youthCouncilId,
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
