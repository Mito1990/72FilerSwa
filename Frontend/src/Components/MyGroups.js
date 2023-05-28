import { useEffect, useState } from "react";
import {useForm} from "react-hook-form"
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"


export const MyGroups = (parentValue) =>{
    const{register, handleSubmit ,getValues} = useForm();
    const[path] = useState("/");
    const[data,setData] = useState([]);
    const[groupID,setGroupID] = useState();
    const[count,setCount] = useState(0);
    const[count2,setCount2] = useState(0);
    const serverToken = Cookies.get('Token');
    const navigate = useNavigate();
    useEffect(()=>{
        const groupRequest = {
            token:serverToken
        }
        fetch('http://localhost:8080/api/groups/get/group', {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
        },
        body: JSON.stringify(groupRequest)
        }).then((response) => response.json()).then((data) => {
            setData(data);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    },[count])
    const NewGroup = ()=>{
        const name = getValues('NewGroup');
        const newGroupRequest = {
            name:name,
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
            setCount(count+1);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }
    const OpenItem =(item)=>{
        navigate(`/share/${item.group_id}`);
        console.log("groupID");
        console.log(item);
        const request = {
            groupID:item.group_id,
            token:serverToken
        }
        fetch('http://localhost:8080/api/groups/get/group/folder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(request)
        }).then((response) => response.json()).then((data) => {
            setData(data);
            setGroupID(item.group_id);
            parentValue.sendDataToParent(data,item.group_id);
            setCount2(count2+1);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
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
            <div className=' flex justify-start m-12 mt-10 w-2/4 flex-wrap'>
                {data.map((item, index) => (
                    <button key={index} className='flex flex-col justify-items-center m-6 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full' onClick={() => OpenItem(item)}>
                    <svg className="h-9 w-9" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512"><path d="M96 128a128 128 0 1 1 256 0A128 128 0 1 1 96 128zM0 482.3C0 383.8 79.8 304 178.3 304h91.4C368.2 304 448 383.8 448 482.3c0 16.4-13.3 29.7-29.7 29.7H29.7C13.3 512 0 498.7 0 482.3zM609.3 512H471.4c5.4-9.4 8.6-20.3 8.6-32v-8c0-60.7-27.1-115.2-69.8-151.8c2.4-.1 4.7-.2 7.1-.2h61.4C567.8 320 640 392.2 640 481.3c0 17-13.8 30.7-30.7 30.7zM432 256c-31 0-59-12.6-79.3-32.9C372.4 196.5 384 163.6 384 128c0-26.8-6.6-52.1-18.3-74.3C384.3 40.1 407.2 32 432 32c61.9 0 112 50.1 112 112s-50.1 112-112 112z"/></svg>
                    <div className='h-5 w-9' key={index}>
                        {item.name}
                    </div>
                    </button>))
                }
            </div>
        </div>
    )
}