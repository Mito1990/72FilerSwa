import Cookies from 'js-cookie';

export const Home = () =>{
    const cookieValue = Cookies.get('ServerToken');
    const data = JSON.parse(cookieValue);
    console.log(data);
    return(
        <div className=' bg-slate-400 h-screen'>
            
        </div>
    ) 
}