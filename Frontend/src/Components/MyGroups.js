import { useEffect, useState } from "react";
import {useForm} from "react-hook-form"
import Cookies from 'js-cookie';
// import {useNavigate } from "react-router-dom"
// const navigate = useNavigate();

export const MyGroups = (dataFromMyGroups) =>{
    const{register, handleSubmit ,getValues} = useForm();
    const serverToken = Cookies.get('Token');
    const NewGroup = async()=>{
        const nameFromInput = getValues('NewGroup');
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
            dataFromMyGroups.dataFromMyGroups(createMemberGroupResponse)
        }).catch((error) => {
            console.error('Error retrieving shareFolder:', error);
        });
    }
    return (
        <div className="flex flex-row items-start">
            <div>
                <form className=" bg-orange-300 flex flex-col w-52 h-32 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(NewGroup)}>
                    <input className="mb-2" type="text" placeholder="Groupname:" name="NewGroup" {...register('NewGroup', { required: true })} />
                    <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">New Group</button>
                </form>
            </div>
        </div>
    )
}