import { useEffect,useState } from "react";
import Cookies from 'js-cookie';
import { SvgRemoveUser } from './svg/SvgGroups';

export const DeleteUserFromGroup = ({group}) =>{
    const[listOfUsersInGroup,setListOfUsersInGroup] = useState([]);
    const[currentGroup,setCurrentGroup] = useState(group);
    const[isOpen, setIsOpen] = useState(false);
    const serverToken = Cookies.get('Token');
    useEffect(()=>{
        setCurrentGroup(group)
    },[group])
    const getListOfUsernamesInGroup = () =>{
        const listOfUsernamesRequest ={
            token:serverToken,
            memberGroupID:currentGroup.memberGroupID
        }
        setIsOpen(true);
        fetch('http://localhost:8080/api/users/get/ListOfUsernamesInGroup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(listOfUsernamesRequest)
        }).then((response) => response.json()).then((ListOfUsernamesResponse) => {
            setListOfUsersInGroup(ListOfUsernamesResponse);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }
    const deleteUser = (item)=>{
        const user = item.target.dataset.item;
        const addUserToGroupRequest = {
            token:serverToken,
            user:user,
            memberGroupID:currentGroup.memberGroupID
        }
        fetch('http://localhost:8080/api/memberGroupController/deleteMemberFromGroup', {
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
    <div className="flex w-full">
        <button className='flex w-full mb-2 justify-center'onClick={getListOfUsernamesInGroup}><SvgRemoveUser></SvgRemoveUser></button>
        {/* <button onClick={getListOfUsernamesInGroup} className="shadow-slate-800 mb-3 shadow-sm w-full text-xs bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded"> Delete User </button> */}
        {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-slate-400 rounded-md shadow-slate-800 shadow-sm m-2 px-6 py-2 flex flex-col justify-center items-center">
            <div className="w-6 h-6 bg-slate-500 rounded-md flex justify-center items-center ml-16"><button onClick={handleClose} className="text-white hover:text-gray-700  m-2">X</button></div>
            <ul className="max-h-96 overflow-y-auto">
                {(listOfUsersInGroup)?(listOfUsersInGroup.map((user, index) => (
                    <button key={index} onClick={deleteUser} data-item={user} className="cursor-pointer mt-2 ml-2 bg-blue-500 hover:bg-blue-700 rounded-md shadow-slate-800 shadow-sm m-2 p-2 flex flex-col justify-start items-start w-32 overflow-hidden whitespace-nowrap overflow-ellipsis">
                    {user}
                    </button>
                ))):<div></div>}
            </ul>
            </div>
        </div>
        )}
    </div>
    );
}

