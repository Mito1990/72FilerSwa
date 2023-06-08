import { useState } from "react";
import { useForm } from "react-hook-form";
import Cookies from 'js-cookie';
import { checkPathForHomeOrShare, getCurrentUrl } from "./GetCurrentURL";

export const CreateNewFolder = ({dataFromCreateNewFolder, currentState}) => {
const { register, handleSubmit, getValues } = useForm();
const [isOpen, setIsOpen] = useState(false);
const serverToken = Cookies.get('Token');
const newFolder = () => {setIsOpen(true);}
const handleClose = () => {setIsOpen(false);};
const onSubmit =async () => {
    const folderName = getValues('Folder');
    if(checkPathForHomeOrShare()==="share"){
        const createNewFolder = {
            token:serverToken,
            folderName:folderName,
            parentFolderID:getCurrentUrl(),
            isShared:true,
        }
        await fetch('http://localhost:8080/api/folder/createNewFolder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
        },
        body: JSON.stringify(createNewFolder)
        }).then((response) => response.json()).then((createNewFolderResponse) => {
        }).catch((error) => {console.error('Error retrieving data:', error); });
    }else{
        const createNewFolder = {
            token:serverToken,
            folderName:folderName,
            parentFolderID:getCurrentUrl(),
            isShared:false,
        }
        await fetch('http://localhost:8080/api/folder/createNewFolder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
        },
        body: JSON.stringify(createNewFolder)
        }).then((response) => response.json()).then((createNewFolderResponse) => {
        }).catch((error) => {console.error('Error retrieving data:', error); });
    }

    setIsOpen(false);
    dataFromCreateNewFolder.dataFromCreateNewFolder()
}
const handleEnter = (e) =>{
    if (e.key === "Enter") {onSubmit()}
}
return (
<div className="flex w-full justify-center mb-2">
<button className="shadow-slate-800 mb-3 shadow-sm w-3/4 m-2 text-xs bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-8 rounded" onClick={newFolder}>New Folder</button>
    {isOpen && (
    <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="p-4">
            <form className="bg-slate-400  w-52 h-52 rounded-md shadow-slate-800 shadow-md m-2 px-6 py-3 flex flex-col justify-center items-center relative" onSubmit={handleSubmit(onSubmit)}>
                <button onClick={handleClose} className= "bg-slate-500 shadow-md rounded-full w-6 h-6  text-white hover:text-black absolute top-4 right-4">X</button>
                <input className="mb-2 mt-6 w-32 rounded-sm shadow-slate-800 shadow-sm" type="text" placeholder="New Folder:" name="username" onKeyDown={handleEnter}  {...register('Folder', { required: true })} />
                <button className="shadow-slate-800 mb-3 shadow-sm w-32 m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded" type="submit">New Folder</button>
            </form>
        </div>
    </div>
    )}
</div>
);
}