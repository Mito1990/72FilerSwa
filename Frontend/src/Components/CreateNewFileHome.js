import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import Cookies from 'js-cookie';

export const CreateNewFileHome = ({dataFromChild, currentFolder }) => {
const { register, handleSubmit, getValues } = useForm();
const [isOpen, setIsOpen] = useState(false);
const serverToken = Cookies.get('Token');
console.error("hello from CreateNewFileHome")
console.error("----------------------------")
console.error("currentFolder")
console.error(currentFolder)

const newFile = () => {
setIsOpen(true);
}

const handleClose = () => {
setIsOpen(false);
};

const onSubmit = async() => {
const filename = getValues('File');
const newFileRequest = {
    token: serverToken,
    name: filename,
    path: currentFolder.path,
    folderID: currentFolder.folder_id,
    parent: currentFolder.folder_id,
    file: true,
    shared:false
}
const response = await fetch('http://localhost:8080/api/folder/new/file', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + serverToken
    },
    body: JSON.stringify(newFileRequest)
})
const data = await response.json();
console.error(data);
dataFromChild.dataFromChild(newFileRequest);
setIsOpen(false);
}
console.error("----------------------------")

return (
<div>
    <button onClick={newFile} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
    New File
    </button>
    {isOpen && (
    <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white rounded p-4">
        <button onClick={handleClose} className="text-gray-500 hover:text-gray-700  m-2">
            X
        </button>
        <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(onSubmit)}>
            <input className="mb-2" type="text" placeholder="File:" name="username" {...register('File', { required: true })} />
            <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
        </form>
        </div>
    </div>
    )}
</div>
);
}
