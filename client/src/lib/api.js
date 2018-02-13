"use strict";

import base64url from "base64url";

const server = ".";

const GET = (path) => fetch(`${server}/${path}`)
.then(r => r.text())
.then(r => base64url.decode(r));

const POST = (path, body) => fetch(`${server}/${path}`, {
	method: "POST",
	body: base64url.encode(body)
})
.then(r => r.text())
.then(r => base64url.decode(r));

const parseFlight = (line) => {
	const obj = {};
	line.trim().split(" ").map(e => e.split("=")).map(([key, val]) => obj[key] = val);
	return obj;
};

const getAirports = () => GET("getAirports").then(d => d.split("\u001F"));
const findRoutes = (dept, dest, time) => POST("findRoutes", [dept, dest, time].join("\u001F"));
const reserveTicket = (name, tickets) => POST("reserveTicket", [name].concat(tickets).join("\u001E"));
const cancelTicket = (body) => POST("cancelTicket", body);
const checkTicket = (body) =>
	Promise.all([
		POST("checkTicket", body)
		.then(d =>
			d.split("\u001E")
			.map(r => r.trim().split("\u001F"))
			.map(([date, flight, row, col, code]) => ({date, flight, row, col, code}))
			.filter(r => r.code)
		),
		GET("getFlights")
		.then(d => d.split("\n"))
		.then(lines => lines.map(parseFlight))
		.then(data => {
			const m = {};
			data.forEach(d => m[d.code] = d);
			return m;
		})
	])
	.then(([rsvs, fMap]) => rsvs.map(r => {
		r.flight = fMap[r.flight];
		return r;
	}));

export default {parseFlight, getAirports, findRoutes, reserveTicket, cancelTicket, checkTicket};
