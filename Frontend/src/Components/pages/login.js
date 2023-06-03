import {useForm} from "react-hook-form"
import {useNavigate} from "react-router-dom"
import Cookies from 'js-cookie';

export const Login = ({ setIsLoggedIn }) =>{
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();

    
   const onSubmit = async(e) =>{
        console.log(e)
      const requestOptions = {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(e)
      };
      try{
        const response = await fetch('http://localhost:8080/api/login/authenticate', requestOptions);
        const data = await response.json();
        Cookies.set('Token', data.message,{expires: 1});
        var date = new Date();
        date.setTime(date.getTime() + (300 * 1000));
        Cookies.set('status', true,{expires: date});
      }catch (error) {
        console.error('Error:', error);
      }
      setIsLoggedIn(Cookies.get('status'))
      navigate("/home"); 
   }

    return(
      <div className="flex justify-center items-center h-screen bg-slate-400">
        <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(onSubmit)}>
            <input className="mb-2" type="text" placeholder="Userame:" name="username" {...register('username', { required: true })}/>
            <input className="mb-2" type="password" placeholder="Password:" name="password" {...register('password', { required: true })}/>
          <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
      </form>
      </div>
    )
}
