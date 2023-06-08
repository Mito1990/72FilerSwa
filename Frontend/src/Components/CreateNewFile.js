import { useState } from "react";
import { useForm } from "react-hook-form";
import Cookies from 'js-cookie';
import { checkPathForHomeOrShare, getCurrentUrl} from "./GetCurrentURL"

export const CreateNewFile = ({dataFromCreateNewFile, currentGroup,}) => {
const { register, handleSubmit, getValues } = useForm();
const [isOpen, setIsOpen] = useState(false);
const serverToken = Cookies.get('Token');
const newFile = () => {setIsOpen(true);}
const handleClose = () => {setIsOpen(false);};
const onSubmit = () => {
    const filename = getValues('File');
    if(checkPathForHomeOrShare()==="share"){
        const newFileRequest = {
            token:serverToken,
            fileName:filename,
            parentFolderID:getCurrentUrl(),
            groupName:currentGroup.groupName,
            memberGroupID:currentGroup.memberGroupID,
            isShared:true,
        }
        fetch('http://localhost:8080/api/folder/createNewFile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + serverToken
            },
            body: JSON.stringify(newFileRequest)
        }).then((response) => response.json()).then((newFileResponse) => {
            dataFromCreateNewFile.dataFromCreateNewFile(newFileResponse);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }else{
        const filename = getValues('File');
        const newFileRequest = {
            token:serverToken,
            fileName:filename,
            parentFolderID:getCurrentUrl(),
            isShared:false,
        }
        fetch('http://localhost:8080/api/folder/createNewFile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + serverToken
            },
            body: JSON.stringify(newFileRequest)
        }).then((response) => response.json()).then((newFileResponse) => {
            dataFromCreateNewFile.dataFromCreateNewFile(newFileResponse);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }
    setIsOpen(false);
}
const handleEnter = (e) =>{
    if (e.key === "Enter") {onSubmit()}
}
return (
<div className="flex w-full justify-center mb-2">
    <button onClick={newFile} className= "shadow-slate-800 text-xs shadow-sm w-3/4 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
    New File
    </button>
    {isOpen && (
    <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="p-4">
        <form className=" bg-slate-400  w-52 h-52 rounded-md shadow-slate-800 shadow-md m-2 px-6 py-3 flex flex-col justify-center items-center relative" onSubmit={handleSubmit(onSubmit)}>
            <button onClick={handleClose} className="bg-slate-500 shadow-md rounded-full w-6 h-6  text-white hover:text-black absolute top-4 right-4"> X   </button>
            <input className="mb-2 mt-6 w-32 rounded-sm shadow-slate-800 shadow-sm" type="text" placeholder="New File:" name="username"onKeyDown={handleEnter} {...register('File', { required: true })} />
            <button className="shadow-slate-800 mb-3 shadow-sm w-32 m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded" type="submit">New File</button>
        </form>
        </div>
    </div>
    )}
</div>
);
}
