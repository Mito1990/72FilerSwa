import { useState,useEffect } from 'react';
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import {useForm} from "react-hook-form"
import { CreateNewFileHome } from './CreateNewFileHome';
import { OpenFileHome } from './OpenFileHome';



export const Home = () => {
  const [count, setCount] = useState(0);
  const [isFolder, setIsFolder] = useState(false);
  const [parent, setParent] = useState(0);
  const [currentFolder, setCurrentFolder] = useState();
  const [path,setPath] = useState("");
  const [data, setData] = useState([]);
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();
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
  return (
    <div className=' h-screen bg-slate-500 flex items-start'>
      <div className='bg-orange-600 m-10'>
        <form className=" bg-orange-300 flex flex-col w-52 h-32 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(AddFolder)}>
            <input className="mb-2" type="text" placeholder="New Folder:" name="NewFOlder" {...register('NewFolder', { required: true })}/>
          <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
        </form>
        <div>
          <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" onClick={shareFolder}>SharedFolder</button>
          {isFolder?<CreateNewFileHome dataFromChild={{dataFromChild}} currentFolder={currentFolder} ></CreateNewFileHome>:[]}
        </div>
      </div>
      <div className=' flex justify-start mt-10 w-2/4 flex-wrap'>
        {data.map((item, index) => (
            !item.file ?
              <button key={index} className='flex flex-col justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full' onClick={() => OpenFolder(item)} >
                <svg className=' h-9 w-9 bg-slate-500' xmlns="http://www.w3.org /2000/svg" viewBox="0 0 512 512"><path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z"/></svg>        
                <div className='h-5 w-9' key={index}>
                {item.name}
                </div>
              </button>
            :
            <OpenFileHome update={{update}} currentFolder={item}></OpenFileHome>
        ))}
      </div>
    </div>
  );
}

const getCurrentFolderIdFromUrl = () => {
  const path2 = window.location.pathname;
  const folderId = path2.substring(path2.lastIndexOf('/') + 1);
  return parseInt(folderId);
};