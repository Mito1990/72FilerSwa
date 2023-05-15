import React, { useEffect, useState } from "react";
import {BrowserRouter,Routes,Route,Navigate} from "react-router-dom";
import { Register } from "./Components/Register"; 
import { Login } from "./Components/Login"; 
import { Home } from "./Components/Home"; 
import Cookies from "js-cookie";
// import { OpenFolder } from "./Components/OpenFolder";
export const App = () =>{
	const[isLoggedIn,setIsLoggedIn]=useState(Cookies.get('status'));
	return (
		<>
		<BrowserRouter>
		<Routes>
			<Route path="/" element={<Navigate replace to="/Login" />} />	
			<Route exact path="/Login" element={<Login setIsLoggedIn={setIsLoggedIn} />}/>
			<Route exact path="/Register" element={<Register/>}/>
			<Route exact path="/Home" element={isLoggedIn ? <Home/> : <Navigate replace to="/Login" />}/>
			<Route exact path="/Home/:name" element={isLoggedIn ? <Home/> : <Navigate replace to="/Login" />}/>
			{/* <Route path="/AddFolder" element={isLoggedIn ? <OddFolder/> : <Navigate replace to="/Login" />}/> */}
		</Routes>
		</BrowserRouter>
		</>
	);
}

export default App;

