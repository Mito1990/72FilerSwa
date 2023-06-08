import {useForm} from "react-hook-form"
import {useNavigate} from "react-router-dom"
import Cookies from 'js-cookie';

export const Login = ({ setIsLoggedIn }) =>{
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();

    
   const onSubmit = async(e) =>{
      const requestOptions = {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(e)
      };
      try{
        const response = await fetch('http://localhost:8080/api/login/authenticate', requestOptions);
        const data = await response.json();
        console.log(data);
        Cookies.set('Token', data.message,{expires: 1});
        Cookies.set('Home', data.homeID,{expires: 1});
        var date = new Date();
        date.setTime(date.getTime() + (300 *2000)); //6 min
        // date.setTime(date.getTime() + (300 *200)); //6 min
        Cookies.set('status', true,{expires: date});
        navigate(`/home/${data.homeID}`);
      }catch (error) {
        console.error('Error:', error);
      }
      setIsLoggedIn(Cookies.get('status'))

   }
   const goToRegister = () =>{
      navigate(`/register`);
   }
    return(
      <div className="flex justify-center items-center h-screen bg-slate-400">
        <form className=" bg-slate-400  w-52 h-52 rounded-md shadow-slate-800 shadow-sm m-2 px-6 py-3 flex flex-col justify-center items-center" onSubmit={handleSubmit(onSubmit)}>
            <input className="mb-2 w-32 rounded-sm shadow-slate-800 shadow-sm" type="text" placeholder="Username:" name="username" {...register('username', { required: true })}/>
            <input className=" w-32 rounded-sm shadow-slate-800 shadow-sm" type="password" placeholder="Password:" name="password" {...register('password', { required: true })}/>
          <button className="shadow-slate-800 mb-2 shadow-sm w-32 m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded" type="submit">Login</button>
          <button className="shadow-slate-800 mb-3 shadow-sm w-32 bg-slate-400 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded" onClick={goToRegister}>Sign UP</button>
      </form>
      </div>
    )
}
