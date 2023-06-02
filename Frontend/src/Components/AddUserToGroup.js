import { useEffect, useState } from "react";
import {useForm} from "react-hook-form"
import Cookies from 'js-cookie';


export const AddUserToGroup = (onChildValue) =>{
    // const {register, handleSubmit ,getValues} = useForm();
    const[data,setData] = useState([]);
    const[data2,setData2] = useState(onChildValue);
    const [isOpen, setIsOpen] = useState(false);
    const serverToken = Cookies.get('Token');
    const getAllUsers = () =>{
        console.log("getAllUsers");
        const listOfUserRequest ={
            token:serverToken,
            groupID:data2.value.group_id
        }
        console.log(listOfUserRequest);
        setIsOpen(true);
        fetch('http://localhost:8080/api/users/get/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(listOfUserRequest)
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
        const addUserToGroupRequest = {
            token:serverToken,
            user:user,
            name:data2.value.name,
            groupID:data2.value.group_id
        }
        fetch('http://localhost:8080/api/groups/add/user/to/group', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(addUserToGroupRequest)
        }).then((response) => response.json()).then((data) => {
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
        handleClose();
    }

    const handleClose = () => {
    setIsOpen(false);
    };
    return (
    <div>
        <button onClick={getAllUsers} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
        Add User
        </button>
        {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-white rounded p-4">
            <button onClick={handleClose} className="text-gray-500 hover:text-gray-700  m-2">
                X
            </button>
            <ul>
                {(data)?(data.map((item, index) => (
                    <button key={index} onClick={addUser} data-item={item} className="cursor-pointer py-2 mt-2 ml-2 flex flex-col justify-center items-center">
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


