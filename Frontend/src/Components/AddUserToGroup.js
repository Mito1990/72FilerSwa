import { useState } from "react";
import Cookies from 'js-cookie';


export const AddUserToGroup = (group) =>{
    const[listOfUserNamesNotAddedToGroup,setListOfUsernamesNotAddedToGroup] = useState([]);
    const [isOpen, setIsOpen] = useState(false);
    const serverToken = Cookies.get('Token');
    const getListOfUsernamesNotAddedToGroup = () =>{
        const listOfUsernamesRequest ={
            token:serverToken,
            memberGroupID:group.currentGroup.memberGroupID
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
            memberGroupID:group.currentGroup.memberGroupID
        }
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
        <button onClick={getListOfUsernamesNotAddedToGroup} className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
        Add User
        </button>
        {isOpen && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-white rounded p-4">
            <button onClick={handleClose} className="text-gray-500 hover:text-gray-700  m-2">
                X
            </button>
            <ul>
                {(listOfUserNamesNotAddedToGroup)?(listOfUserNamesNotAddedToGroup.map((user, index) => (
                    <button key={index} onClick={addUser} data-item={user} className="cursor-pointer py-2 mt-2 ml-2 flex flex-col justify-center items-center">
                        {user}
                    </button>
                ))):(<div></div>)}
            </ul>
            </div>
        </div>
        )}
    </div>
    );
}


