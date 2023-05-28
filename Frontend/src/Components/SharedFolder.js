import { useState,useEffect } from 'react';
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import {useForm} from "react-hook-form"
import { MyGroups } from './MyGroups';
import { AddUserToGroup} from './AddUserToGroup';
import { GroupAndFolder } from './GroupAndFolder';



export const SharedFolder = () => {
  const [count, setCount] = useState(0);
  const[count2,setCount2] = useState(0);
  const [currentIDURL, setCurrentIDURL] = useState(0);
  const [childValue, setChildValue] = useState([]);
  const [groupID, setGroupID] = useState(0);
  const [Currentfolder, setCurrentFolder] = useState(0);
  const [path,setPath] = useState("/");
  const [data, setData] = useState([]);
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();
  const serverToken = Cookies.get('Token');
  useEffect(() => {
    const handlePopstate = () => {
      let currentID = getCurrentFolderIdFromUrl();
      console.log("currentID");
      console.log(currentID);
      if(isNaN(currentID)){
        currentID = 0;
        setCurrentIDURL(currentID);
      }else{
        setCurrentIDURL(currentID);
      }
      setCount2(count2+1);
    };
       // Attach the handlePopstate function to the popstate event
    window.addEventListener('popstate', handlePopstate);
    // Clean up the event listener on component unmount
    return () => {
      window.removeEventListener('popstate', handlePopstate);
    };
  }, [count]);

  const AddFolder = (e) =>{
    let currentID = getCurrentFolderIdFromUrl();
    const folder = {
        token:serverToken,
        groupID:groupID,
        name:e.NewFolder,
        parent:currentID,
        path:path+"/"+e.NewFolder,
        shared:true
    }
    console.log("folder: "+ folder);
    fetch('http://localhost:8080/api/groups/add/folder/to/group', {
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
  useEffect(()=>{
    const request = {
      groupID:currentIDURL,
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
      setChildValue(data);
    }).catch((error) => {
        console.error('Error retrieving data:', error);
    });
  },[count])

  const handleChildValue = (value,group_id) => {
    setGroupID(group_id);
    setCurrentIDURL(group_id);
    setChildValue(value);
  };
  const OpenFolder = (item) =>{
    setCurrentFolder(item);
    navigate(`/share/${item.folder_id}`);
    const folderRequest ={
      parentID:item.folder_id,
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
    setPath(item.path);
  }).catch((error) => {
    console.error('Error retrieving data:', error);
  });
  }
  return (
    <div className=' h-screen bg-slate-500 flex items-start'>
      <div className='bg-orange-600 m-10 flex flex-row'>
            <div>{(currentIDURL===0)?(<MyGroups sendDataToParent={handleChildValue}></MyGroups>):(
              <><form className=" bg-orange-300 flex flex-col w-52 h-32 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(AddFolder)}>
              <input className="mb-2" type="text" placeholder="New Folder:" name="NewFOlder" {...register('NewFolder', { required: true })} />
              <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
            </form><AddUserToGroup value={groupID}></AddUserToGroup></>
            )}
            </div>
            <div className=' flex justify-start mt-10 w-2/4 flex-wrap'>
              {currentIDURL!==0?childValue.map((item, index) => (
              <button key={index} className='flex flex-col justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full' onClick={() => OpenFolder(item)} >
                <svg className=' h-9 w-9 bg-slate-500' xmlns="http://www.w3.org /2000/svg" viewBox="0 0 512 512"><path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z"/></svg>        
                <div className='h-5 w-9' key={index}>
                {item.name}
                </div>
              </button>
              )):[]}
            </div>
      </div>
    </div>
  );
}

const getCurrentFolderIdFromUrl = () => {
  const path2 = window.location.pathname;
  const folderId = path2.substring(path2.lastIndexOf('/') + 1);
  return parseInt(folderId);
};