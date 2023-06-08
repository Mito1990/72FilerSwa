import { useState,useEffect } from 'react';
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import { CreateNewFolder} from "./CreateNewFolder"
import {AddUserToGroup} from "./AddUserToGroup"
import {DeleteUserFromGroup} from "./DeleteUserFromGroup"
import { CreateNewFile } from './CreateNewFile';
import { MapItem } from './MapItem';
import { getCurrentUrl} from "./GetCurrentURL"
import { CreateNewGroup } from './CreateNewGroup';
import { Logout } from './Logout';


export const SharedFolder = () => {
  const [currentState,setCurrentState] = useState();
  const[currentGroup,setCurrentGroup]=useState();
  const [updateMapItem, setUpdateMapItem] = useState(0);
  const navigate = useNavigate();
  const homeID = Cookies.get('Home');
  useEffect(() => {
    const handlePopstate = async () => {
      setUpdateMapItem(updateMapItem+1);
    };
    window.addEventListener('popstate', handlePopstate);
    return () => {
      window.removeEventListener('popstate', handlePopstate);
    };
  }, []);

  const dataFromCreateNewGroup = async(group) => {
    setUpdateMapItem(updateMapItem+1)
  };

  const dataFromCreateNewFile = (data) =>{
    setUpdateMapItem(updateMapItem+1)
  }
  const dataFromCreateNewFolder = (data) =>{
    setUpdateMapItem(updateMapItem+1)
  }
const dataFromMapItem = async(parentFolder) =>{
  setCurrentState(parentFolder);
}
const homeFolder = () =>{
  navigate(`/home/${homeID}`);
}
const handleGroup = (group) =>{
  console.log("hanldeGroup -> shared")
  console.log(group)
  setCurrentGroup(group)
}
  return (
<div name="main" className='h-screen w-screen bg-slate-400 flex flex-row relative p-4'>
      <div className='absolute top-3 right-9'><Logout></Logout></div>
      {(getCurrentUrl()==="share") ? (
        <div name="Panel" className='mt-12 flex flex-row m-4 w-1/6 justify-center items-start h-5/6 rounded-md'>
          <div className='flex flex-col w-full h-full'>
              <div className='flex flex-col w-full justify-center items-center h-full'>
                <div className='bg-slate-500 flex flex-col  w-full justify-start items-center h-full rounded-md shadow-2xl'>
                  <div className='w-5/6'><CreateNewGroup dataFromCreateNewGroup={dataFromCreateNewGroup}></CreateNewGroup></div>
                  <div className='bg-slate-400  rounded-md shadow-slate-800 shadow-sm m-2 w-3/4 px-6 py-3 flex flex-col justify-center items-center'>
                    <button className="shadow-slate-800 mb-3 shadow-sm w-full m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded" onClick={homeFolder}>Home</button>
                  </div>
                </div>
              </div>
          </div>
        </div>
        )
        :
        (
          <div name="Panel" className='bg-slate-500 mt-12 flex flex-row m-4 w-1/6 justify-center items-start h-5/6 rounded-md shadow-2xl'>
            <div className='flex flex-col w-full'>
              <div className='flex flex-col w-full justify-center items-center'>
                <CreateNewFolder dataFromCreateNewFolder={{ dataFromCreateNewFolder }} currentState={currentState}></CreateNewFolder>
                <CreateNewFile dataFromCreateNewFile={{ dataFromCreateNewFile }} currentGroup={currentGroup}></CreateNewFile>
                <div className='bg-slate-400  rounded-md shadow-slate-800 shadow-sm m-2 w-3/4 px-6 py-3 flex flex-col justify-center items-center'>
                  <button className="shadow-slate-800 mb-3 shadow-sm w-full m-2 bg-blue-500 hover:bg-blue-700 text-white text-center font-bold py-2 px-4 rounded" onClick={homeFolder}>Home</button>
                </div>
                <div className='bg-slate-400  rounded-md shadow-slate-800 shadow-sm m-2 w-3/4 py-3 flex flex-col justify-center items-center'>
                  <div className='flex w-3/4 justify-center'><AddUserToGroup group={currentGroup}></AddUserToGroup></div>
                  <div className='flex w-3/4 justify-center'><DeleteUserFromGroup group={currentGroup}></DeleteUserFromGroup></div>
                </div>
              </div>
            </div>
          </div>
        )}
        <div className='bg-slate-500 mt-12 flex flex-row h-5/6 w-1/4 m-4  items-start rounded-md shadow-2xl flex-grow flex-shrink flex-wrap'>
          <MapItem dataFromMapItem={{ dataFromMapItem }} updateFromParent={{ updateMapItem }} handleGroup={{handleGroup}}></MapItem>
        </div>
    </div>
    );

}