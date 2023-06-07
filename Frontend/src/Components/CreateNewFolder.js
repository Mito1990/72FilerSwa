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
            console.error("CreateNewFolder ->createNewFolderResponse")
            console.error(createNewFolderResponse)
        }).catch((error) => {console.error('Error retrieving data:', error); });
    }

    setIsOpen(false);
    dataFromCreateNewFolder.dataFromCreateNewFolder()
}
return (
<div className="flex w-full justify-center mb-2">
<button className="shadow-slate-800 mb-3 shadow-sm w-3/4 m-2 text-xs bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-8 rounded" onClick={newFolder}>New Folder</button>
    {isOpen && (
    <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white rounded p-4">
        <button onClick={handleClose} className="text-gray-500 hover:text-gray-700  m-2">
            X
        </button>
        <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(onSubmit)}>
            <input className="mb-2" type="text" placeholder="File:" name="username" {...register('Folder', { required: true })} />
            <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
        </form>
        </div>
    </div>
    )}
</div>
);
}
