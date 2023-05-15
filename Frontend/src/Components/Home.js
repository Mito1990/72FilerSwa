import { useState,useEffect } from 'react';
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import {useForm} from "react-hook-form"



export const Home = () => {
  const [count, setCount] = useState(0);
  const [pa, setPa] = useState(0);
  const [path, setPath] = useState("/");
  const [data, setData] = useState([]);
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();
  const serverToken = Cookies.get('Token');

  useEffect(() => {
    const toSend ={
      parentID:pa,
      token:serverToken
    }
    fetch('http://localhost:8080/api/folder/get/all', {
      method: 'POST',
      headers: {
      'Content-Type': 'application/json',
        'Authorization': 'Bearer '+serverToken
      },
      body: JSON.stringify(toSend)
      }).then((response) => response.json()).then((data) => {
        setData(data);
      }).catch((error) => {
        console.error('Error retrieving data:', error);
      });
  }, [count]);
  useEffect(() => {
    const toSend ={
      parentID:pa,
      token:serverToken
    }
    const handlePopstate = () => {
      fetch('http://localhost:8080/api/folder/get/all', {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
          'Authorization': 'Bearer '+serverToken
        },
        body: JSON.stringify(toSend)
        }).then((response) => response.json()).then((data) => {
          setData(data);
        }).catch((error) => {
          console.error('Error retrieving data:', error);
        });

    };
    // Attach the handlePopstate function to the popstate event
    window.addEventListener('popstate', handlePopstate);

    // Clean up the event listener on component unmount
    return () => {
      window.removeEventListener('popstate', handlePopstate);
    };
  }, []);

  const AddFolder = (e) =>{
    console.log(e.NewFolder);
    const serverToken = Cookies.get('Token');
    const folder = {
      name:e.NewFolder,
      parent:pa,
      path:path+e.NewFolder,
      token:serverToken
    }
    console.log(folder);
    fetch('http://localhost:8080/api/folder/new', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer '+serverToken
      },
      body: JSON.stringify(folder)
    }).then((response) => response.json()).then((data) => {
    console.log(data);
      setCount(count+1);
   }).catch((error) => {
    console.error('Error retrieving data:', error);
    });
  }


  const OpenFolder = (item) =>{
    const serverToken = Cookies.get('Token');
    const newUrl = `/home/${item.name}`;
    // Use history.push to navigate to the new URL
    navigate(newUrl);
    const toSend ={
      parentID:item.folder_id,
      path:item.path,
      token:serverToken
    }
    console.log(toSend);
    fetch('http://localhost:8080/api/folder/get/all', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+serverToken
    },
    body: JSON.stringify(toSend)
  }).then((response) => response.json()).then((data) => {
    console.log(data);
    setPa(item.folder_id);
    setPath(item.path+"/");
    setCount(count+1);
  }).catch((error) => {
    console.error('Error retrieving data:', error);
  });
  }

  return (
    <div className=' h-screen bg-slate-500 flex items-start'>
      <div className='bg-orange-600 m-10'>
        <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(AddFolder)}>
            <input className="mb-2" type="text" placeholder="New Folder:" name="NewFOlder" {...register('NewFolder', { required: true })}/>
          <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
        </form> 
      </div>
      <div className=' flex justify-start mt-10 w-2/4 flex-wrap'>
        {data.map((item, index) => (
            <button key={index} className='flex flex-col justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full' onClick={() => OpenFolder(item)} >
              <svg className=' h-9 w-9 bg-slate-500' xmlns="http://www.w3.org /2000/svg" viewBox="0 0 512 512"><path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z"/></svg>        
              <div className='h-5 w-9' key={index}>
              {/* {item.name} */}
              {item.name}
              </div>
            </button>
        ))}
      </div>
        {/* <button onClick={OpenFolder} data={Object.entries(home)}>OpenFolder</button> */}
    </div>
  );
}

