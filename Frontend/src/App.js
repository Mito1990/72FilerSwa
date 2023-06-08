import React, { useState } from "react";
import {BrowserRouter,Routes,Route,Navigate} from "react-router-dom";
import { SignUp } from "./Components/Signup";
import { Login } from "./Components/Login";
import { Home } from "./Components/Home";
import { SharedFolder } from "./Components/SharedFolder";
import Cookies from "js-cookie";
export const App = () =>{
	const[isLoggedIn,setIsLoggedIn]=useState(Cookies.get('status'));
	return (
		<>
		<BrowserRouter>
		<Routes>
			<Route path="/" element={<Navigate replace to="/login" />} />
			<Route exact path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} />}/>
			<Route exact path="/register" element={<SignUp/>}/>
			{/* <Route exact path="/home" element={isLoggedIn ? <Home/> : <Navigate replace to="/login" />}/> */}
			<Route exact path="/share" element={isLoggedIn ? <SharedFolder/> : <Navigate replace to="/login" />}/>
			<Route exact path="/share/:ID" element={isLoggedIn ? <SharedFolder/> : <Navigate replace to="/login" />}/>
			{/* <Route exact path="/share/:group/:ID" element={isLoggedIn ? <SharedFolder/> : <Navigate replace to="/login" />}/> */}
			<Route exact path="/home/:ID" element={isLoggedIn ? <Home/> : <Navigate replace to="/login" />}/>
		</Routes>
		</BrowserRouter>
		</>
	);
}

export default App;
