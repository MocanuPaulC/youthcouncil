//includes.js
export function getUser(needsLogin) {
	let userId = undefined;
	try {
		userId = document.getElementById("authUserId").value;
	} catch (e) {
		console.log("User not logged in");
		if (needsLogin) {
			window.location.href = "/login";
		}
	}
	return userId;
}