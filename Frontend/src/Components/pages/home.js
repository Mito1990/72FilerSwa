import { useState,useEffect } from 'react';
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import {useForm} from "react-hook-form"
import { CreateNewFileHome } from '../CreateNewFileHome';
import { OpenFileHome } from '../OpenFileHome';



export const Home = () => {
const [count, setCount] = useState(0);
const [storeItem, setStoreItem] = useState(0);
const [isFolder, setIsFolder] = useState(false);
const [popUpFolder, setPopUpFolder] = useState(false);
const [parent, setParent] = useState(0);
const [currentFolder, setCurrentFolder] = useState();
const [path,setPath] = useState("");
const [isClicked, setIsClicked] = useState(false);
const [rename, setRename] = useState('');
const [data, setData] = useState([]);
const {register, handleSubmit} = useForm();
const [contextMenuPosition, setContextMenuPosition] = useState({ x: window.innerWidth / 2, y: window.innerHeight / 2 });const navigate = useNavigate();
const serverToken = Cookies.get('Token');

useEffect(() => {
console.error("first")
const handlePopstate = () => {
console.error("second")

let currentID = getCurrentFolderIdFromUrl();
if(isNaN(currentID)){
currentID = 0;
setParent(currentID);
setIsFolder(false);
}else{
setParent(currentID);
}
const folderRequest ={
parentID:currentID,
token:serverToken
}
fetch('http://localhost:8080/api/folder/get/all', {
method: 'POST',
headers: {
'Content-Type': 'application/json',
'Authorization': 'Bearer '+serverToken
},
body: JSON.stringify(folderRequest)
}).then((response) => response.json()).then((data) => {
setData(data);
}).catch((error) => {
console.error('Error retrieving data:', error);
});
};
const folderRequest ={
parentID:parent,
token:serverToken
}
fetch('http://localhost:8080/api/folder/get/all', {
method: 'POST',
headers: {
'Content-Type': 'application/json',
'Authorization': 'Bearer '+serverToken
},
body: JSON.stringify(folderRequest)
}).then((response) => response.json()).then((data) => {
setData(data);
console.error("data from useeffect");
console.error(data);
}).catch((error) => {
console.error('Error retrieving data:', error);
});
// Attach the handlePopstate function to the popstate event
window.addEventListener('popstate', handlePopstate);
// Clean up the event listener on component unmount
return () => {
window.removeEventListener('popstate', handlePopstate);
};
}, [count]);

const AddFolder = (e) =>{
const folder = {
	name:e.NewFolder,
	parent:parent,
	path:path+"/"+e.NewFolder,
	token:serverToken,
	shared:false
}
fetch('http://localhost:8080/api/folder/new', {
	method: 'POST',
	headers: {
	'Content-Type': 'application/json',
	'Authorization': 'Bearer '+serverToken
	},
	body: JSON.stringify(folder)
}).then((response) => response.json()).then((data) => {
	setCount(count+1);
}).catch((error) => {
console.error('Error retrieving data:', error);
});
}

const OpenFolder = async (item) =>{
console.error("hello form openFOlder in Home")
console.error("-----------------------------")
console.error("item")
console.error(item)
setIsFolder(true);
setCurrentFolder(item);
setParent(item.folder_id);
setPath(item.path);
navigate(`/home/${item.folder_id}`);
const folderRequest ={
	parentID:item.folder_id,
	token:serverToken
}
try{
	const response = await fetch('http://localhost:8080/api/folder/get/all', {
	method: 'POST',
	headers: {
		'Content-Type': 'application/json',
		'Authorization': 'Bearer '+serverToken
	},
	body: JSON.stringify(folderRequest)
	})
	const data = await response.json();
	console.error("data from response from openFolder")
	console.error(data)
	setData(data)
	}catch(error){
	console.error('Error retrieving data:', error);
	};
	console.error("-----------------------------")

}

const shareFolder = () =>{
navigate("/share");
}
const dataFromChild = async(CurrentFolder)=>{
console.error("hello form datafromchild")
console.error("-------------------------")
const folderRequest ={
	parentID:CurrentFolder.folderID,
	token:serverToken
}
console.error(folderRequest);
try{
	const response = await fetch('http://localhost:8080/api/folder/get/all', {
	method: 'POST',
	headers: {
	'Content-Type': 'application/json',
	'Authorization': 'Bearer '+serverToken
	},
	body: JSON.stringify(folderRequest)
	})
	const data = await response.json();
	console.error(data)
	setData(data);
	} catch(error) {
	console.error('Error retrieving data:', error);
	};
console.error("-------------------------")
};
const update = async(response) =>{
console.error("hello form update");
console.error(response)
setData(response);
}
const handleContextMenu = (event,index,item) =>{
setPopUpFolder(true);
console.warn("hello");
setStoreItem(item);
console.error(item);

}
const handleMenuItemClick = (action) => {
if (action === 'rename') {
	setClicked(true);
	console.log('Rename action clicked');
} else if (action === 'delete') {
	setPopUpFolder(false);

	console.log('Delete action clicked');
}
};
const handleRenameFile =(e)=>{
	setRename(e.target.value);
	console.log(rename);
}
const setClicked = ()=>{
	setIsClicked(true);
}
const handleKeyDown = async (e) => {
	console.error("hello from handleKeyDown")
	if (e.keyCode === 13) {
		setPopUpFolder(false);
		setIsClicked(false);
		const request = {
			name:rename,
			oldName:storeItem.name,
			folderID:storeItem.folder_id,
			path:storeItem.path,
			parent:storeItem.parent,
			shared:storeItem.shared,
			file:storeItem.file,
			token:serverToken
		}
		console.error("handleKeyDown")
		console.error(request)
		try{
			const response = await fetch('http://localhost:8080/api/folder/file/rename', {
			method: 'POST',
			headers: {
			'Content-Type': 'application/json',
			'Authorization': 'Bearer '+serverToken
			},
			body: JSON.stringify(request)
			})
			const data = await response.json();
			console.error("handleKeyDown => data")
			console.error(data)
			setData(data);
			} catch(error) {
			console.error('Error retrieving data:', error);
			};
	}
};
return (
<div className="h-screen bg-slate-500 flex items-start">
	<div className="bg-orange-600 m-10">
	<form
		className="bg-orange-300 flex flex-col w-52 h-32 justify-center items-center rounded-md shadow-2xl"
		onSubmit={handleSubmit(AddFolder)}
	>
		<input
		className="mb-2"
		type="text"
		placeholder="New Folder:"
		name="NewFOlder"
		{...register('NewFolder', { required: true })}
		/>
		<button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">
		Submit
		</button>
	</form>
	<div>
		<button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" onClick={shareFolder}>
		SharedFolder
		</button>
		{isFolder ? (
		<CreateNewFileHome dataFromChild={{ dataFromChild }} currentFolder={currentFolder}></CreateNewFileHome>
		) : (
		[]
		)}
	</div>
	</div>
	<div className="flex justify-start mt-10 w-3/4 h-96 flex-wrap bg-purple-500">
	{data.map((item, index) =>
		!item.file ? (
		<button
			key={index}
			onContextMenu={(event) => handleContextMenu(event, index,item)}
			className="flex flex-col justify-items-center m-2 sm:w-2/3 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full"
			onClick={() => OpenFolder(item)}
		>
			<svg
			className="h-9 w-9 bg-slate-500"
			xmlns="http://www.w3.org/2000/svg"
			viewBox="0 0 512 512"
			>
			<path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z" />
			</svg>
			<div className="h-5 w-9" key={index}>
			{item.name}
			</div>
		</button>
		) : (
		<div className="w-full" key={index}>
			<OpenFileHome update={{ update }} currentFolder={item}></OpenFileHome>
		</div>
		)
	)}
{popUpFolder && (
	<div
		className="fixed top-0 left-0 w-screen h-screen flex items-center justify-center bg-gray-900 bg-opacity-50"
	>
		<div className="bg-white border border-gray-300 shadow-lg p-4">
		{!isClicked ? (
			<button className="px-3 py-1 text-xl font-medium hover:bg-blue-600 text-black rounded-md" onClick={() => handleMenuItemClick('rename')}>
			Rename
			</button>
		) : (
			<input className="text-slate-950 border-2 border-slate-950" onKeyDown={handleKeyDown} type="text" value={rename} onChange={handleRenameFile} />
		)}
		<button className="px-4 py-2" onClick={() => handleMenuItemClick('delete')}>
			Delete
		</button>
		<button className="px-4 py-2" onClick={() => setPopUpFolder(false)}>
			Close
		</button>
		</div>
	</div>
	)}
	</div>
</div>
);

}

const getCurrentFolderIdFromUrl = () => {
const path2 = window.location.pathname;
const folderId = path2.substring(path2.lastIndexOf('/') + 1);
return parseInt(folderId);
};