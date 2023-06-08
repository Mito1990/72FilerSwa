import {useNavigate} from "react-router-dom"
export const SessionExpiredPopUp = () =>{
const navigate = useNavigate();

const handleClose =()=>{
    navigate("/Login");
}
    return(
        <div className="flex justify-center items-center h-screen bg-slate-400">
            <div className="fixed inset-0 flex items-center justify-center z-50">
                <div className="bg-slate-400  rounded-md shadow-slate-800 shadow-sm m-2 px-6 py-3 flex flex-col justify-center items-center">
                    <p className="text-white ">Session has expired</p>
                    <p className="text-white ">Login in to continue</p>
                    <button onClick={handleClose} className="shadow-slate-800 mb-3 shadow-sm w-32 m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded">OK</button>
                </div>
            </div>
        </div>
    );
}
