import {useForm} from "react-hook-form"
import {useNavigate} from "react-router-dom"

export const Register = () =>{
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();


   const onSubmit = async(e) =>{
        console.log(e)
      const requestOptions = {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(e)
      };
      const response = await fetch('http://localhost:8080/api/login/register', requestOptions);
      const data = await response.json();
      console.log(data);
      navigate("/Login");
   }

    return(
      <div className="flex justify-center items-center h-screen bg-slate-400">
        <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(onSubmit)}>
            <input className="mb-2" type="text" placeholder="Name:" name="name" {...register('name', { required: true })} />
            <input className="mb-2" type="text" placeholder="Userame:" name="username" {...register('username', { required: true })}/>
            <input className="mb-2" type="password" placeholder="Password:" name="password" {...register('password', { required: true })}/>
          <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
      </form>
      </div>
    )
}

	
