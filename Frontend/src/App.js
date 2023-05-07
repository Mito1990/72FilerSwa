import React from "react";
import {BrowserRouter,Routes,Route,Navigate} from "react-router-dom";
import { Register } from "./Components/Register"; 
import { Login } from "./Components/Login"; 
import { Home } from "./Components/Home"; 

export const App = () =>{
	
return (
	<>
	<BrowserRouter>
	<Routes>
		<Route path="/" element={<Navigate replace to="/Login" />} />	
		<Route exact path="/Login" element={<Login/>}/>
		<Route exact path="/Register" element={<Register/>}/>
		<Route exact path="/Home" element={<Home/>}/>
	</Routes>
	</BrowserRouter>
	</>
);
}

export default App;

