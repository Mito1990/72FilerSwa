import { useEffect, useState } from "react";
import {useForm} from "react-hook-form"
import Cookies from 'js-cookie';


export const MyGroups = ({onChildValue}) =>{
    const {register, handleSubmit ,getValues} = useForm();
    const[path] = useState("/");
    const[data,setData] = useState(0);
    const serverToken = Cookies.get('Token');
    const NewGroup = ()=>{
        console.log("first");
        const name = getValues('NewGroup');
        const newGroupRequest = {
            groupname:name,
            parent:0,
            path:path+name,
            token:serverToken,
            shared:true
        }
        fetch('http://localhost:8080/api/groups/create/group', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(newGroupRequest)
        }).then((response) => response.json()).then((data) => {
            console.log("second ");
            console.log(data);
            setData(data);
            onChildValue(data);

        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }

    return (
        <div>
            <form className=" bg-orange-300 flex flex-col w-52 h-32 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(NewGroup)}>
                <input className="mb-2" type="text" placeholder="Groupname:" name="NewGroup" {...register('NewGroup', { required: true })}/>
                <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">New Group</button>
            </form>
        </div>
    )
}