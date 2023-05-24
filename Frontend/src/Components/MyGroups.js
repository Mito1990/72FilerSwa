import { useEffect, useState } from "react";
import {useForm} from "react-hook-form"
import Cookies from 'js-cookie';


export const MyGroups = ({data}) =>{
    const {register, handleSubmit ,getValues} = useForm();
    const[group,setGroup] = useState({});
    const[count,setCount] = useState(0);
    const[createGroup,setCreateGroup] = useState({});
    const[data2,setData2] = useState();
    const serverToken = Cookies.get('Token');
    console.log(data);
    // const createNewGroup = {
       
    // }
 
    // useEffect(()=>{
    //     fetch('http://localhost:8080/api/groups/create/group', {
    //         method: 'POST',
    //         headers: {
    //           'Content-Type': 'application/json',
    //           'Authorization': 'Bearer '+serverToken
    //         },
    //         body: JSON.stringify(createGroup)
    //       }).then((response) => response.json()).then((data) => {
    //         setData2(data);
    //       }).catch((error) => {
    //         console.error('Error retrieving data:', error);
    //       });
    // },[count])
        const NewGroup = ()=>{
            const h = {
                groupname:getValues('NewGroup'),
                folderID:data.folder_id,
                token:serverToken
            }
            console.log("data");
            console.log(h);
            fetch('http://localhost:8080/api/groups/create/group', {
                method: 'POST',
                headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
                },
                body: JSON.stringify(h)
            }).then((response) => response.json()).then((data2) => {
                setData2(data2);
                console.log(data2);
            }).catch((error) => {
                console.error('Error retrieving data:', error);
            });
        // setGroup({
        //     folderID:data.folderID,
        //     path:data.path,
        //     GroupRequest:{
        //         groupname:data.register,
        //         token:serverToken 
        //     },
        //     MemberRequest:{
        //         username:"user2"
        //     }
        // })
    }

    return (
        <div>
            <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(NewGroup)}>
                <input className="mb-2" type="text" placeholder="Groupname:" name="NewGroup" {...register('NewGroup', { required: true })}/>
                <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">New Group</button>
            </form> 
        </div>
    )
}