import React from "react";
import Header from "../components/Header";
import Section from "../components/Section";
import NavBar from "../components/NavBar";
import shipogleLogo from "../assets/shipogleLogo.png";
export default function Home() {
  return (
    <div className="Homepage">
      <NavBar />
      <Header title="S H I P O G L E" />
      <center>
        <img alt="logo" src={shipogleLogo} width="200px" height="200px"></img>
      </center>

      <Section
        title="Who are we?"
        info="Lorem ipsum dolor sit amet, consectetur adipiscing elit. In tempus elit mi, ac finibus sapien rhoncus at. In luctus ultrices quam, ut auctor enim mattis quis. Donec semper elit a hendrerit cursus. Maecenas pharetra massa mi, quis dapibus ligula tristique non. Aenean at lacus ipsum. Sed magna sapien, accumsan nec orci id, eleifend sodales ipsum. Integer pulvinar lacinia nisi, non tempor turpis tincidunt quis. Quisque fermentum fringilla mauris, ac cursus libero facilisis in. Aliquam nec dui lorem. Nulla erat augue, rhoncus eu mauris et, viverra consectetur elit. Integer in diam quis nisl elementum imperdiet ut in tellus. Nam finibus vestibulum diam, eu feugiat eros laoreet ut."
      />
    </div>
  );
}
