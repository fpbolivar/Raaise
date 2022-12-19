import React, { Component } from "react";
import dynamic from "next/dynamic";
const Chart = dynamic(() => import("react-apexcharts"), { ssr: false });

class Graph extends Component {
  constructor(props) {
    super(props);
    this.state = {
      name:this.props.name,
      options: {
        chart: {id: "basic-bar"},  // to show the bar graph
        xaxis: {                   // to show the Months on x-axis
          categories:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",]
        },
      },
      series: [
        {
          name: this.props.name,           //name of the series
          data: this.props.data.map(item=>{return item.value}),
        },
      ],
    };
  }
  componentDidUpdate(prevProps) {  //to update the data of graph 
    if (this.props.data !== prevProps.data) {
      this.setState({series: [
        {
          name: this.props.name,
          data: this.props.data && this.props.data.length> 0 &&  this.props.data.map(item=>{return item.value}),
        },
      ],
      })
    }
  }
  render() {
    const {series,name,options}=this.state
    return (
      <div className="app graph-section">
        <h3 style={{marginLeft: "15px"}}> {name} </h3>
        <div className="row">
          <div className="mixed-chart">
            <Chart options={options} series={series} type="bar" width="100%"/>
          </div>
        </div>
      </div>
    );
  }
}
export default Graph;
