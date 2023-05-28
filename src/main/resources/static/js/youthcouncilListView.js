const svg_map = document.getElementById("svg-map");
const councils = document.querySelectorAll(".yc-card");
const size = 100;

for (const council of councils) {
	const name = council.getAttribute("name");
	const path_result =  adjustPath(svg_map.querySelector(`path[n="${name}"]`), 16);

	if (path_result.error) {
		console.error(path_result.error);
		continue;
	}
	const path = path_result.node;

	const svg_container = council.querySelector(".yc-map-container");

	const g = document.createElement("g");

	const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
	svg.setAttribute("width", 100);
	svg.setAttribute("height", 100);
	svg.setAttribute("viewBox", "0 0 16 16");
	svg.classList.add("d-flex", "justify-content-center", "align-items-center")


	console.log(path);
	svg.append(path);
	svg_container.append(svg);
}

/**
 * @param path_element {Node}
 * @param max_size {number}
 * @return {{
 *     node: Node,
 *     error: string
 * }}
 */
function adjustPath(path_element, max_size) {
	if (!path_element) return {error: "The passed path element is null!", node: undefined}
	if (!max_size) return {error: "The passed max_size is null!", node: undefined}

	const path_string = path_element.getAttribute("d");
	const out_node = path_element.cloneNode(true);

	if (!path_string) {
		return {
			error: "Element does not have a 'd' Attribute defining the path!",
			node: undefined
		}
	}
	if (!path_string.includes("M") && !path_string.includes("Z")) {
		return {
			error: "Path of the element is not of the correct format",
			node: undefined
		}
	}
	const numbers = path_string
		.substring(2, path_string.length)
		.substring(0, path_string.length - 4)
		.split(" ")
		.map(n => parseFloat(n));

	let x_min = Math.max(...numbers);
	let x_max = 0;
	let y_min = Math.max(...numbers);
	let y_max = 0;
	for (let i = 0; i < numbers.length; i++) {
		if (i % 2) {
			y_min = numbers[i] < y_min ? numbers[i] : y_min;
			y_max = numbers[i] > y_max ? numbers[i] : y_max;
		} else {
			x_min = numbers[i] < x_min ? numbers[i] : x_min;
			x_max = numbers[i] > x_max ? numbers[i] : x_max;
		}
	}
	for (let i = 0; i < numbers.length; i++) {
		if (i % 2) {
			numbers[i] = ((numbers[i] - y_min) / (y_max - y_min)) * max_size;
		} else {
			numbers[i] = ((numbers[i] - x_min) / (x_max - x_min)) * max_size;
		}
	}

	const path = "M " + numbers
		.map(n => n.toString())
		.reduce((a, b) => a + " " + b) + " Z";

	out_node.setAttribute("d", path);
	out_node.setAttribute("stroke", "#000000");
	out_node.setAttribute("stroke-width", 0);
	return {
		error: undefined,
		node: out_node
	}
}