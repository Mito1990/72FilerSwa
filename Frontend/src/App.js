import React from "react";
import Navbar from './Components/Navbar/Navbar';
import {BrowserRouter,Routes,Route,Navigate} from "react-router-dom";
import { SignUp } from "./Components/pages/signup"; 
import { Login } from "./Components/pages/login"; 
import Home from './Components/pages/home';
import About from './Components/pages/about';
import Filebase from './Components/pages/filebase';
import Teams from './Components/pages/teams';

export const App = () =>{
	
return (
	<>
	<BrowserRouter>
	<Navbar />
	<Routes>
		<Route exact path="/Home" element={<Home/>}/>
		<Route exact path="/Teams" element={<Teams/>}/>
		<Route exact path="/Filebase" element={<Filebase/>}/>
		<Route exact path="/About" element={<About/>}/>
		<Route exact path="/Login" element={<Login/>}/>
		<Route exact path="/SignUp" element={<SignUp/>}/>
 	</Routes>
	</BrowserRouter>
	</>
);
}

export default App;
