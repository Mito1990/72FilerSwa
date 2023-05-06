import { useState } from "react";
export const Register = () =>{
  const[registerRequest,setRegisterRequest] = useState({
    name:"",
    username:"",
    password:""
  });
  const[registerResponse,setRegisterResponse] = useState();

   const handleSubmit = (e) =>{
      e.preventDefault();
      console.log(registerRequest);
      const requestOptions = {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(registerRequest)
      };
      fetch('http://localhost:8080/api/login/register', requestOptions)
          .then(response => response.json(registerRequest))
          .then(data => setRegisterResponse(data));
          console.log(registerResponse);
   }

    return(
      <div className="flex justify-center items-center h-screen bg-slate-400">
        <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-xl" onSubmit={handleSubmit}>
          <input className="mb-2" placeholder="name:" name="name:" type="text" value={registerRequest.name} onChange={(e)=>setRegisterRequest({...registerRequest,name:e.target.value})}/>
          <input className=" mb-2" placeholder="username:" type="text" value={registerRequest.username} onChange={(e)=>setRegisterRequest({...registerRequest,username:e.target.value})}/>
          <input className="mb-2" placeholder="password" type="password" value={registerRequest.password} onChange={(e)=>setRegisterRequest({...registerRequest,password:e.target.value})}/>
          <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
      </form>
      </div>
    )
}