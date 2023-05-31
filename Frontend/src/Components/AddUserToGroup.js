import { useEffect, useState } from "react";
import {useForm} from "react-hook-form"
import Cookies from 'js-cookie';


export const AddUserToGroup = (onChildValue) =>{
    const {register, handleSubmit ,getValues} = useForm();
    const[path,setPath] = useState("/");
    const[data,setData] = useState([]);
    const[data2,setData2] = useState(onChildValue);
    const [isOpen, setIsOpen] = useState(false);
    const serverToken = Cookies.get('Token');
    const getAllUsers = () =>{
        console.log(data2);
        setIsOpen(true);
        fetch('http://localhost:8080/api/users/get/users', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
            },
            // body: JSON.stringify(newGroupRequest)
        }).then((response) => response.json()).then((data) => {
            console.log("second ");
            console.log(data);
            setData(data);
            // onChildValue(data);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }
    const addUser = (item)=>{
        const user = item.target.dataset.item;
        console.log("hello");
        console.log(data2);
        console.log(data2.value.name);
        console.log(data2.value.group_id);
        console.log(onChildValue);
        const newGroupRequest = {
            token:serverToken,
            user:user,
            name:data2.value.name,
            groupID:data2.value.group_id
        }
        console.log(newGroupRequest);
        fetch('http://localhost:8080/api/groups/add/user/to/group', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(newGroupRequest)
        }).then((response) => response.json()).then((data) => {
            console.log("second ");
            console.log(data);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }

    const handleClose = () => {
    setIsOpen(false);
    };
    return (
    <div>
        <button onClick={getAllUsers} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
        Open Popup
        </button>
        {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-white rounded p-4">
            <button onClick={handleClose} className="text-gray-500 hover:text-gray-700  m-2">
                Close
            </button>
            <ul>
                {(data)?(data.map((item, index) => (
                    <button key={index} onClick={addUser} data-item={item} className="cursor-pointer py-2">
                        {item}
                    </button>
                ))):(<div></div>)}
            </ul>
            </div>
        </div>
        )}
    </div>
    );
}
    // return (
    //     <div>
    //         <form className=" bg-orange-300 flex flex-col w-52 h-32 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(getAllUsers)}>
    //             {/* <input className="mb-2" type="text" placeholder="Add User:" name="NewGroup" {...register('Add User', { required: true })}/> */}
    //             <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">+</button>
    //         </form>
    //         {(data)?(
    //             <ul>
    //             {data.map((item, index) => (
    //                 <li key={index} onClick={() => console.log(item)}>
    //                     {item}
    //                 </li>
    //             ))}
    //         </ul>
    //     ):(<div></div>)}
    //     </div>
    // )
// }

