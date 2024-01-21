import {useForm} from "react-hook-form"
import { useState } from 'react';
import Cookies from 'js-cookie';

export const CreateNewGroup = (dataFromCreateNewGroup)=>{
    const{register, handleSubmit ,getValues} = useForm();
    const [isOpen, setIsOpen] = useState(false);
    const handleOpen = () => {setIsOpen(true);}
    const handleClose = () => {setIsOpen(false);};
    const serverToken = Cookies.get('Token');
    const createGroup = async()=>{
        handleClose();
        const nameFromInput = getValues('GroupName');
        const createMemberGroupRequest = {
            token:serverToken,
            groupName:nameFromInput,
        }
        await fetch('http://localhost:8080/api/memberGroupController/createMemberGroup', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
        },body: JSON.stringify(createMemberGroupRequest)
        }).then((response) => response.json()).then((createMemberGroupResponse) => {
            dataFromCreateNewGroup.dataFromCreateNewGroup(createMemberGroupResponse)
        }).catch((error) => {
            console.error('Error retrieving shareFolder:', error);
        });
    }
    const handleEnter = (e) =>{
        if (e.key === "Enter") {createGroup()}
    }
    return (
    <div className=" flex w-full justify-center">
    <button className="shadow-slate-800 mb-3 shadow-sm w-full m-2 text-xs bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-8 rounded" onClick={handleOpen}>New Group</button>
        {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="rounded p-4">
            <form className=" bg-slate-400  w-52 h-52 rounded-md shadow-slate-800 shadow-md m-2 px-6 py-3 flex flex-col justify-center items-center relative" onSubmit={handleSubmit(createGroup)}>
                <button onClick={handleClose} className="bg-slate-500 shadow-md rounded-full w-6 h-6  text-white hover:text-black absolute top-4 right-4"> X </button>
                <input className="mb-2 mt-6 w-32 rounded-sm shadow-slate-800 shadow-sm" type="text" placeholder="Group name:" name="username"onKeyDown={handleEnter}  {...register('GroupName', { required: true })} />
                <button className="shadow-slate-800 mb-3 shadow-sm w-32 m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded" type="submit">New Group</button>
            </form>
            </div>
        </div>
        )}
    </div>
    );
}
