import {useForm} from "react-hook-form"
import { useState,useEffect } from 'react';
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
    return (
    <div className=" flex w-full justify-center">
    <button className="shadow-slate-800 mb-3 shadow-sm w-full m-2 text-xs bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-8 rounded" onClick={handleOpen}>New Group</button>
        {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-white rounded p-4">
            <button onClick={handleClose} className="text-gray-500 hover:text-gray-700  m-2">
                X
            </button>
            <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(createGroup)}>
                <input className="mb-2" type="text" placeholder="Group name:" name="username" {...register('GroupName', { required: true })} />
                <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
            </form>
            </div>
        </div>
        )}
    </div>
    );
}
