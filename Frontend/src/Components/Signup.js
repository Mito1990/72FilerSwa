import { useState } from "react";
import {useForm} from "react-hook-form"
import {useNavigate} from "react-router-dom"

export const SignUp = () =>{
  const {register, handleSubmit} = useForm();
  const [userExistsAlready,setUserExistsAlready] = useState(false);
  const navigate = useNavigate();


   const onSubmit = async(e) =>{
      fetch('http://localhost:8080/api/login/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(e)
      }).then((response) => response.json()).then((data) => {
        console.log(data);
        if(data.userExists===true){
          setUserExistsAlready(true);
        }else{
          navigate("/Login");
        }
      }).catch((error) => {
        console.error('Error retrieving data:', error);
      });
   }
const handleClose =()=>{
  setUserExistsAlready(false);
}
    return(
        <div className="flex justify-center items-center h-screen bg-slate-400">{
            userExistsAlready ?
              <div className="fixed inset-0 flex items-center justify-center z-50">
                <div className="bg-slate-400  rounded-md shadow-slate-800 shadow-sm m-2 px-6 py-3 flex flex-col justify-center items-center">
                  <p className="text-white ">User exists already!</p>
                   <p className="text-white ">Please choose another username</p>
                  <button onClick={handleClose} className="shadow-slate-800 mb-3 shadow-sm w-32 m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded">OK</button>
                </div>
              </div>
              :
            <form className=" bg-slate-400  w-52 h-52 rounded-md shadow-slate-800 shadow-sm m-2 px-6 py-3 flex flex-col justify-center items-center" onSubmit={handleSubmit(onSubmit)}>
                <input className="mb-2 w-32 rounded-sm shadow-slate-800 shadow-sm" type="text" placeholder="Name:" name="name" {...register('name', { required: true })} />
                <input className="mb-2 w-32 rounded-sm shadow-slate-800 shadow-sm" type="text" placeholder="Userame:" name="username" {...register('username', { required: true })}/>
                <input className="mb-2 w-32 rounded-sm shadow-slate-800 shadow-sm" type="password" placeholder="Password:" name="password" {...register('password', { required: true })}/>
              <button className="shadow-slate-800 mb-3 shadow-sm w-32 m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded" type="submit">Sign UP</button>
          </form>
        }
      </div>
    )
}

