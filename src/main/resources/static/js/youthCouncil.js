import csrfHeader from "./csrfHeader.js";

const {name, value} = csrfHeader();

const button = document.querySelector(".join-council");

button.addEventListener("click", addUserToCouncil);


function addUserToCouncil() {

	const youthCouncilId = document.getElementById("youthCouncilId").value;
	const userId = document.getElementById("authUserId").value;
	fetch(`/api/youthcouncils/${youthCouncilId}/${userId}`, {
		method: "PATCH",
		headers: {
			"Content-Type": "application/json",
			[name]: value
		}
	})
		.then((response) => {
			console.log(response);
		});
}
