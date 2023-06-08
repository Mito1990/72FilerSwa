import { useEffect, useState } from "react";
import Cookies from 'js-cookie';
import { SvgAddUser } from './svg/SvgGroups';


export const AddUserToGroup = ({group}) =>{
    const[listOfUserNamesNotAddedToGroup,setListOfUsernamesNotAddedToGroup] = useState([]);
    const[currentGroup,setCurrentGroup] = useState(group);
    const [isOpen, setIsOpen] = useState(false);
    const serverToken = Cookies.get('Token');
    useEffect(()=>{setCurrentGroup(group)},[group])
    const getListOfUsernamesNotAddedToGroup = () =>{
        const listOfUsernamesRequest ={
            token:serverToken,
            memberGroupID:currentGroup.memberGroupID
        }
        setIsOpen(true);
        fetch('http://localhost:8080/api/users/get/ListOfUsernamesNotAddedToGroup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(listOfUsernamesRequest)
        }).then((response) => response.json()).then((ListOfUsernamesResponse) => {
            console.warn(ListOfUsernamesResponse);
            setListOfUsernamesNotAddedToGroup(ListOfUsernamesResponse);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }
    const addUser = (userFromList)=>{
        const user = userFromList.target.dataset.item;
        const addUserToGroupRequest = {
            token:serverToken,
            user:user,
            memberGroupID:currentGroup.memberGroupID
        }
        console.warn(addUserToGroupRequest)
        fetch('http://localhost:8080/api/memberGroupController/addUserToMemberGroup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(addUserToGroupRequest)
        }).then((response) => response.json()).then((responseFromAddUserToMemberGroup) => {
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
        handleClose();
    }

    const handleClose = () => {
    setIsOpen(false);
    };
    return (
    <div className="flex w-full">
        <button className='flex w-full mb-2 justify-center'onClick={getListOfUsernamesNotAddedToGroup}><SvgAddUser></SvgAddUser></button>
        {/* <button onClick={getListOfUsernamesNotAddedToGroup} className="shadow-slate-800 mb-3  text text-xs shadow-sm w-full bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded"> Add User</button> */}
        {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-slate-400 rounded-md shadow-slate-800 shadow-sm m-2 px-6 py-2 flex flex-col justify-center items-center">
            <div className="w-6 h-6 bg-slate-500 rounded-md flex justify-center items-center ml-16"><button onClick={handleClose} className="text-white hover:text-gray-700  m-2">X</button></div>
            <ul className="max-h-96 overflow-y-auto">
                {(listOfUserNamesNotAddedToGroup)?(listOfUserNamesNotAddedToGroup.map((user, index) => (
                    <button key={index} onClick={addUser} data-item={user} className="cursor-pointer mt-2 ml-2 bg-blue-500 hover:bg-blue-700 rounded-md shadow-slate-800 shadow-sm m-2 p-2 flex flex-col justify-start items-start w-32 overflow-hidden overflow-ellipsis">
                    {user} </button>
                ))):[]}
            </ul>
            </div>
        </div>
        )}
    </div>
    );
}